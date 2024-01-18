package com.waminiyi.realestatemanager.features.editestate

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.Status
import com.waminiyi.realestatemanager.databinding.FragmentEditestateBinding
import com.waminiyi.realestatemanager.features.agentEntities
import com.waminiyi.realestatemanager.features.editestate.agent.AgentAdapter
import com.waminiyi.realestatemanager.features.editestate.estatetype.EstateTypeAdapter
import com.waminiyi.realestatemanager.features.editestate.poi.PoiAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EditEstateFragment : Fragment() {

    private val viewModel: EditEstateViewModel by viewModels()

    private var _binding: FragmentEditestateBinding? = null

    private var estateId: String? = null

    private val binding get() = _binding!!

    companion object {
        const val ARG_ESTATE_ID = "estate_id"

        fun newInstance(estateId: String?): EditEstateFragment {
            val fragment = EditEstateFragment()
            val args = Bundle()
            args.putString(ARG_ESTATE_ID, estateId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()

                menuInflater.inflate(R.menu.edit_fragment_appbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun updateUi(uiState: EditEstateUiState) {
        updateProgressBarView(uiState.isLoading || uiState.isEstateSaving)
        updateErrorView(uiState.savingError.isNotBlank(), uiState.savingError)

        if (uiState.mainPhoto == null) {
            binding.coverPhotoLabel.visibility = View.GONE
            binding.deleteMainPhotoButton.visibility = View.GONE
        } else {
            binding.coverPhotoLabel.visibility = View.VISIBLE
            binding.deleteMainPhotoButton.visibility = View.VISIBLE
        }

        when {
            uiState.isLoading -> {
                Log.d("UISTATE", "LOADING")
                binding.estateSavingErrorTextView.visibility = View.GONE
            }

            uiState.savingError.isNotBlank() -> {
                Log.d("UISTATE", "ERROR")
                binding.estateSavingErrorTextView.visibility = View.VISIBLE
                binding.estateSavingErrorTextView.text = uiState.savingError
            }

            uiState.isEstateSaving -> {
                Log.d("UISTATE", "NOTEMPTY")
                binding.estateSavingErrorTextView.visibility = View.VISIBLE
            }

            else -> {
                Log.d("UISTATE", "EMPTY")
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
        estateId = arguments?.getString(ARG_ESTATE_ID)
        viewModel.savedStateHandle[ARG_ESTATE_ID] = estateId

        val fragmentScope = CoroutineScope(Dispatchers.Main)

        fragmentScope.launch {
            viewModel.uiState.collect { uiState ->
                updateUi(uiState)
            }
        }
        binding.saveEstateButton.setOnClickListener {
            viewModel.saveEstate()
        }
        setUpEstateTypeView()
        setUpPoiView()
        setUpAgentsView()
        setUpTextChangedListener(
            binding.aboutEditText,
            handleNewValue = { viewModel.setMainPhotoDescription(it) })
        setUpTextChangedListener(
            binding.areaEditText,
            handleNewValue = { viewModel.setArea(it.toFloat()) })
        setUpTextChangedListener(
            binding.roomCountEditText,
            handleNewValue = { viewModel.setRoomsCount(it.toInt()) })
        setUpTextChangedListener(
            binding.priceEditText,
            handleNewValue = { viewModel.setPrice(it.toInt()) })
        setUpTextChangedListener(
            binding.descriptionEditText,
            handleNewValue = { viewModel.setFullDescription(it) })

        configureStatusView(context)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateProgressBarView(isLoading: Boolean) {
        if (isLoading) {
            binding.circularProgressBar.visibility = View.VISIBLE
        } else {
            binding.circularProgressBar.visibility = View.GONE
        }
    }

    private fun updateErrorView(hasError: Boolean, errorMessage: String?) {
        if (hasError) {
            Log.d("UISTATE", "ERROR")
            binding.estateSavingErrorTextView.visibility = View.VISIBLE
            binding.estateSavingErrorTextView.text = errorMessage
        } else {
            binding.estateSavingErrorTextView.visibility = View.GONE
        }
    }

    private fun setUpEstateTypeView() {
        val recyclerView = binding.estateTypeRecyclerView
        val adapter = EstateTypeAdapter(EstateType.entries, onTypeSelected = {
            viewModel.setType(it)
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //TODO("Use uistate infos to set initial selected value")
        adapter.setCurrentType(EstateType.APARTMENT)

    }

    private fun setUpPoiView() {
        val recyclerView = binding.poiRecyclerView
        val adapter = PoiAdapter(PointOfInterest.entries, onPoiSelected = {
            viewModel.setPoiList(it)
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        //TODO("Use uistate infos to set initial selected value")

    }

    private fun setUpAgentsView() {
        val recyclerView = binding.agentRecyclerView
        val adapter = AgentAdapter(agentEntities.map { it.asAgent() }, onAgentSelected = {
            viewModel.setAgent(it)
        })
        //TODO("Use uistate infos to set initial selected value")

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpTextChangedListener(view: EditText, handleNewValue: (String) -> Unit) {
        view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString()
                handleNewValue(enteredText)
            }
        })
    }

    private fun configureStatusView(context: Context?) {
        val statusOptions = resources.getStringArray(R.array.estate_status)
//TODO (configure the spinner to not change the status without user interaction)
        context?.let {
            val adapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, statusOptions)
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
                        val status = Status.valueOf(statusOptions[position].uppercase())
                        viewModel.setStatus(status)
                        Log.d("NEWSTATUS", status.name)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
        }
    }
}