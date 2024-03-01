package com.waminiyi.realestatemanager.features.editestate

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.Constants
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.toRawString
import com.waminiyi.realestatemanager.core.util.util.createAddressFromPlace
import com.waminiyi.realestatemanager.core.util.util.getFormattedDate
import com.waminiyi.realestatemanager.databinding.FragmentEditestateBinding
import com.waminiyi.realestatemanager.features.editestate.agent.AgentAdapter
import com.waminiyi.realestatemanager.features.editestate.estatetype.EstateTypeAdapter
import com.waminiyi.realestatemanager.features.editestate.photo.PhotoAdapter
import com.waminiyi.realestatemanager.features.editestate.poi.PoiAdapter
import com.waminiyi.realestatemanager.features.extensions.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class EditEstateFragment : Fragment() {

    //region Variables initialization
    private val viewModel: EditEstateViewModel by viewModels()
    private var _binding: FragmentEditestateBinding? = null
    private var estateId: String? = null
    private lateinit var agentAdapter: AgentAdapter
    private lateinit var estateTypeAdapter: EstateTypeAdapter
    private lateinit var poiAdapter: PoiAdapter
    private lateinit var photoAdapter: PhotoAdapter
    private var isInitialStatusSpinnerSelection = true
    private val binding get() = _binding!!
    private val startAutocomplete = registerForPlaceSearchCallBack()
    private var temporaryCapturedImageUri: Uri? = null
    private val multiplePhotosLauncher = registerForPhotosPickingResult()
    private val cameraLauncher = registerForCameraResult()

    //endregion

    companion object {
        fun newInstance(estateId: String?) = EditEstateFragment().apply {
            arguments = Bundle().apply {
                putString(Constants.ARG_ESTATE_ID, estateId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditestateBinding.inflate(inflater, container, false)
        val root: View = binding.root
        estateId = arguments?.getString(Constants.ARG_ESTATE_ID)

        val fragmentScope = CoroutineScope(Dispatchers.Main)

        fragmentScope.launch {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.uiState.collect { uiState ->
                    updateUi(uiState)
                }
            }
        }

        binding.addPhotoButton.setOnClickListener {
            showPhotoInputOptionsDialog()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClickListeners()
        setUpTextChangedListeners()
        setUpPhotosRecyclerView()
        setUpEstateTypeRecyclerView()
        setUpPoiRecyclerView()
        setUpAgentsRecyclerView()
        setupEstateStatusSpinner()
        binding.saveButton.setOnClickListener {
            viewModel.saveEstate()
        }
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
            //TODO: show dialog on click and handle phone back press too
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //region UI updates
    private fun updateUi(uiState: EditEstateUiState) {
        when {
            uiState.isLoading || uiState.isEstateSaving -> {
                binding.circularProgressBar.visibility = View.VISIBLE
            }

            !uiState.savingError.isNullOrEmpty() -> {
                showSavingErrorDialog(uiState.savingError)
            }

            uiState.isEstateSaved -> {
                showEstateSavedDialog()
            }

            else -> {
                binding.circularProgressBar.visibility = View.GONE
                updateDetailsView(uiState)
            }
        }
    }

    private fun updateDetailsView(uiState: EditEstateUiState) {
        binding.areaTextInputEditText.updateValue(uiState.area?.toString().orEmpty())
        binding.areaTextInputLayout.error = uiState.areaError

        binding.roomsCountTextInputEditText.updateValue(uiState.roomsCount?.toString().orEmpty())
        binding.roomsCountTextInputLayout.error = uiState.roomsCountError

        binding.bedroomsCountTextInputEditText.updateValue(uiState.bedroomsCount?.toString().orEmpty())
        binding.bedroomsCountTextInputLayout.error = uiState.roomsCountError

        binding.bathroomsCountTextInputEditText.updateValue(uiState.bathroomsCount?.toString().orEmpty())

        binding.priceTextInputEditText.updateValue(uiState.price?.toString().orEmpty())
        //TODO:Change price type to long or handle error
        binding.priceTextInputLayout.error = uiState.priceError

        binding.descriptionTextInputEditText.updateValue(uiState.fullDescription.orEmpty())

        binding.entryDateTextView.text = getFormattedDate(uiState.entryDate)
        binding.entryDateErrorTextView.text = uiState.dateError

        binding.saleDateTextView.text = getFormattedDate(uiState.saleDate)
        binding.saleDateErrorTextView.text = uiState.dateError

        binding.addressTextView.text = uiState.address?.toRawString().orEmpty()
        binding.addressErrorTextView.text = uiState.addressError

        agentAdapter.setCurrentAgent(uiState.agent)
        binding.agentErrorTextView.text = uiState.agentError

        estateTypeAdapter.setCurrentType(uiState.type)
        binding.typeErrorTextView.text = uiState.typeError

        poiAdapter.setSelectedPoiList(uiState.nearbyPointsOfInterest)
        photoAdapter.submitList(uiState.photos)
        agentAdapter.submitList(uiState.agentsList)
        binding.photoErrorTextView.text = uiState.photosError

        binding.statusSpinner.setSelection(
            when (uiState.estateStatus) {
                EstateStatus.AVAILABLE -> 0
                EstateStatus.SOLD -> 1
            }
        )
        binding.statusErrorTextView.text = uiState.statusError


        Log.d("UISTATE", uiState.toString())
    }
    //endregion

    // region Listeners
    private fun setUpClickListeners() {
        binding.addressTextView.setOnClickListener {
            launchPlaceAutocompleteActivity()
        }

        binding.entryDateTextView.setOnClickListener {
            showDatePickerDialog(onDatePicked = {
                viewModel.setEntryDate(it)
            })
        }

        binding.saleDateTextView.setOnClickListener {
            showDatePickerDialog(onDatePicked = {
                viewModel.setSaleDate(it)
            })
        }
    }

    private fun setUpTextChangedListeners() {
        binding.areaTextInputEditText.afterTextChanged {
            viewModel.setArea(
                if (it.isNotBlank()) {
                    it.toInt()
                } else {
                    null
                }
            )
        }
        binding.roomsCountTextInputEditText.afterTextChanged {
            viewModel.setRoomsCount(
                if (it.isNotBlank()) {
                    it.toInt()
                } else {
                    null
                }
            )
        }
        binding.bedroomsCountTextInputEditText.afterTextChanged {
            viewModel.setBedroomsCount(
                if (it.isNotBlank()) {
                    it.toInt()
                } else {
                    null
                }
            )
        }
        binding.bathroomsCountTextInputEditText.afterTextChanged {
            viewModel.setBathroomsCount(
                if (it.isNotBlank()) {
                    it.toInt()
                } else {
                    null
                }
            )
        }

        binding.priceTextInputEditText.afterTextChanged {
            viewModel.setPrice(
                if (it.isNotBlank()) {
                    it.toInt()
                } else {
                    null
                }
            )
        }

        binding.descriptionTextInputEditText.afterTextChanged { viewModel.setFullDescription(it) }
    }

    //endregion
    private fun TextInputEditText.updateValue(newValue: String) {
        with(this) {
            setText(newValue)
            setSelection(newValue.length)
        }
    }

    // region RecyclerViews
    private fun setUpPhotosRecyclerView() {
        val recyclerView = binding.photoRecyclerView
        photoAdapter = PhotoAdapter(
            onPhotoDeleted = { viewModel.addPhotoToDeletionList(it) },
            onDescriptionEditionStarted = { index, currentDescription ->
                showPhotoDescriptionEditionDialog(
                    currentDescription,
                    onDescriptionUpdated = { newDescription ->
                        viewModel.updatePhotoDescription(index, newDescription)
                    })
            },
            onItemMove = { fromPosition, toPosition ->
                viewModel.swapPhotoItems(fromPosition, toPosition)
            }
        )
        recyclerView.adapter = photoAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val itemTouchHelperCallback = PhotoAdapter.PhotoItemTouchHelperCallback(photoAdapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setUpEstateTypeRecyclerView() {
        val recyclerView = binding.estateTypeRecyclerView
        estateTypeAdapter = EstateTypeAdapter(
            estateTypes = EstateType.entries,
            onTypeSelected = { viewModel.setType(it) }
        )
        recyclerView.adapter = estateTypeAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpPoiRecyclerView() {
        val recyclerView = binding.poiRecyclerView
        poiAdapter = PoiAdapter(
            poiList = PointOfInterest.entries,
            onPoiSelected = { viewModel.setPoiList(it) }
        )
        recyclerView.adapter = poiAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpAgentsRecyclerView() {
        val recyclerView = binding.agentRecyclerView
        agentAdapter = AgentAdapter(
            onAgentSelected = { viewModel.setAgent(it) }
        )

        recyclerView.adapter = agentAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//        viewLifecycleOwner.lifecycleScope.launch {
//            val agents = viewModel.loadAgents()
//
//            agentAdapter.submitList(agents)
//            Log.d("agents", agentAdapter.itemCount.toString())
//
//
//        }
        //TODO: retrieve agents from database

    }


    private fun setupEstateStatusSpinner() {
        val statusOptions = resources.getStringArray(R.array.estate_status)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.statusSpinner.adapter = adapter

        binding.statusSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (!isInitialStatusSpinnerSelection) {
                        val estateStatus =
                            EstateStatus.valueOf(statusOptions[position].uppercase())
                        viewModel.setStatus(estateStatus)
                    }
                    isInitialStatusSpinnerSelection = false
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
    }
    //endregion

    //region Place search

    private fun launchPlaceAutocompleteActivity() {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG
        )

        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(requireContext())
        startAutocomplete.launch(intent)
    }

    private fun registerForPlaceSearchCallBack() =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    viewModel.setAddress(createAddressFromPlace(place))
                    //TODO: handle the case the selected address doesn't match the good format
                }
            }
        }

    //endregion

    //region Photos picking

    private fun registerForPhotosPickingResult() =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
            uris?.let {
                if (uris.isNotEmpty()) {
                    viewModel.addPhotos(uris)
                }
            }
        }

    private fun registerForCameraResult() =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                temporaryCapturedImageUri?.let { uri ->
                    viewModel.addPhotos(listOf(uri))
                }
                temporaryCapturedImageUri = null
            }
        }

    private fun createImageFile(context: Context): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_${timeStamp}"
        return File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$fileName.jpg")
    }

    //endregion

    //region Dialogs

    private fun showPhotoDescriptionEditionDialog(
        currentDescription: String?,
        onDescriptionUpdated: (String?) -> Unit
    ) {
        val view = LayoutInflater.from(requireActivity())
            .inflate(R.layout.photo_description_edition_layout, null, false)
        val editText = view.findViewById<TextInputEditText>(R.id.descriptionDialogTextInputEditText)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.update_description))
            .setView(view)
            .setCancelable(true)
            .create()
        editText.setText(currentDescription)
        currentDescription?.let { editText.setSelection(it.length) }

        view.findViewById<Button>(R.id.saveDescriptionButton).setOnClickListener {
            dialog.dismiss()
            onDescriptionUpdated(editText.text.toString())
        }
        dialog.show()
    }

    private fun showPhotoInputOptionsDialog() {
        val view = LayoutInflater.from(requireActivity())
            .inflate(R.layout.photo_input_options_layout, null, false)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_photos))
            .setView(view)
            .setCancelable(true)
            .create()

        view.findViewById<MaterialTextView>(R.id.optionLibraryTextView).setOnClickListener {
            dialog.dismiss()
            multiplePhotosLauncher.launch(Constants.IMAGE)
        }

        view.findViewById<MaterialTextView>(R.id.optionCameraTextView).setOnClickListener {
            dialog.dismiss()
            temporaryCapturedImageUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                createImageFile(requireContext())
            )
            cameraLauncher.launch(temporaryCapturedImageUri)
        }
        dialog.show()
    }

    private fun showSavingErrorDialog(errorMessage: String?) {
        binding.circularProgressBar.visibility = View.GONE
        errorMessage.let {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.estate_saving_error_dialog_title))
                .setMessage(it)
                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            viewModel.resetError()
        }
    }

    private fun showEstateSavedDialog() {
        binding.circularProgressBar.visibility = View.GONE
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Estate saved")
            .setMessage("Your estate has been save")
            .setPositiveButton("OK") { dialog, _ ->
                viewModel.resetSavingStatus()
                dialog.dismiss()
                findNavController().navigateUp()
            }
            .show()
    }

    private fun showDatePickerDialog(onDatePicked: (Date) -> Unit) {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selectedDate ->
            onDatePicked(Date(selectedDate))
        }

        picker.show(parentFragmentManager, picker.toString())
    }

    //endregion
}