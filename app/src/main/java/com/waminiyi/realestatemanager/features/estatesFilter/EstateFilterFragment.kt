package com.waminiyi.realestatemanager.features.estatesFilter

import android.os.Bundle
import com.waminiyi.realestatemanager.core.Constants.FILTER_ROOM_COUNT_THRESHOLD
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.Timeframe
import com.waminiyi.realestatemanager.core.util.remdispatchers.Dispatcher
import com.waminiyi.realestatemanager.core.util.remdispatchers.RemDispatchers
import com.waminiyi.realestatemanager.databinding.FragmentEstateFilterBinding
import com.waminiyi.realestatemanager.features.extensions.afterTextChanged
import com.waminiyi.realestatemanager.features.extensions.updateValue
import com.waminiyi.realestatemanager.features.model.asUiEstateType
import com.waminiyi.realestatemanager.features.model.asUiPointOfInterest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_PHOTO_COUNT_MAX_VALUE = 6

@AndroidEntryPoint
class EstateFilterFragment : Fragment() {

    private var _binding: FragmentEstateFilterBinding? = null
    private val binding get() = _binding!!
    private val filterViewModel: EstateFilterViewModel by viewModels()

    @Dispatcher(RemDispatchers.IO)
    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher
    private lateinit var availableStatesAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstateFilterBinding.inflate(inflater, container, false)
        availableStatesAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
        setUpCityAutocompleteTextView()
        viewLifecycleOwner.lifecycleScope.launch {
            filterViewModel.uiState.collect { uiState ->
                updateUi(uiState)
            }
        }

        binding.buttonApplyFilters.setOnClickListener {
            filterViewModel.saveFilter()
        }
        binding.buttonClearFilter.setOnClickListener {
            filterViewModel.clearFilter()
        }

        binding.minPriceTextInputEditText.afterTextChanged { price ->
            if (price.isBlank()) {
                filterViewModel.updateMinPrice(null)
            } else {
                filterViewModel.updateMinPrice(price.toInt())
            }
        }
        binding.maxPriceTextInputEditText.afterTextChanged { price ->
            if (price.isBlank()) {
                filterViewModel.updateMaxPrice(null)
            } else {
                filterViewModel.updateMaxPrice(price.toInt())
            }
        }
        binding.minAreaTextInputEditText.afterTextChanged { area ->
            if (area.isBlank()) {
                filterViewModel.updateMinArea(null)
            } else {
                filterViewModel.updateMinArea(area.toInt())
            }
        }
        binding.maxAreaTextInputEditText.afterTextChanged { area ->
            if (area.isBlank()) {
                filterViewModel.updateMaxArea(null)
            } else {
                filterViewModel.updateMaxArea(area.toInt())
            }
        }

        return binding.root
    }

    private fun updateUi(uiState: FilterUiState) {
        availableStatesAdapter.clear()
        availableStatesAdapter.addAll(uiState.usCities)
        updateSelectedCitiesChipGroup(uiState.filter.cities)
        if (uiState.isLoadedUiState) {
            updateEstateTypeFilterOptions(uiState.filter.estateTypes)
            updateNumberOfRoomsFilterOptions(uiState.filter.roomsCounts)
            updateNumberOfBedRoomsFilterOptions(uiState.filter.bedroomsCounts)
            updateNumberOfPhotosChoiceOptions(uiState.filter.photosMinimalCount)
            updatePoiFilterOptions(uiState.filter.pointOfInterest)
            updateStatusChoiceOptions(uiState.filter.estateStatus)
            updateEntryDateChoiceOptions(uiState.filter.entryDateTimeframe)
            updateSaleDateChoiceOptions(uiState.filter.saleDateTimeframe)
            binding.minPriceTextInputEditText.updateValue(
                uiState.filter.minPrice?.toString().orEmpty()
            )
            binding.maxPriceTextInputEditText.updateValue(
                uiState.filter.maxPrice?.toString().orEmpty()
            )
            binding.minAreaTextInputEditText.updateValue(
                uiState.filter.minArea?.toString().orEmpty()
            )
            binding.maxAreaTextInputEditText.updateValue(
                uiState.filter.maxArea?.toString().orEmpty()
            )
        }

        if (uiState.isSaved) {
            findNavController().navigateUp()
        }
    }

    private fun updateEstateTypeFilterOptions(selectedTypes: List<EstateType>) {
        binding.estateTypeChipGroup.removeAllViews()

        val estateTypes = EstateType.entries

        for (estateType in estateTypes) {
            val chip = createChip(
                estateType.asUiEstateType(requireContext()).name,
                estateType.asUiEstateType(requireContext()).iconResId
            )
            chip.isChecked = estateType in selectedTypes
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    filterViewModel.addEstateType(estateType)
                } else {
                    filterViewModel.removeEstateType(estateType)
                }
            }
            binding.estateTypeChipGroup.addView(chip)
        }
    }

    private fun updateNumberOfRoomsFilterOptions(selectedNumbers: List<Int>) {
        binding.roomChipGroup.removeAllViews()

        for (count in 1..FILTER_ROOM_COUNT_THRESHOLD) {
            val chip = createChip(
                count.asFilterRoomCountText()
            )
            chip.isChecked = count in selectedNumbers
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    filterViewModel.addRoomsCount(count)
                } else {
                    filterViewModel.removeRoomsCount(count)
                }
            }
            binding.roomChipGroup.addView(chip)
        }
    }

    private fun updateNumberOfBedRoomsFilterOptions(selectedNumbers: List<Int>) {
        binding.bedroomChipGroup.removeAllViews()

        for (count in 1..FILTER_ROOM_COUNT_THRESHOLD) {
            val chip = createChip(
                count.asFilterRoomCountText()
            )
            chip.isChecked = count in selectedNumbers
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    filterViewModel.addBedroomsCount(count)
                } else {
                    filterViewModel.removeBedroomsCount(count)
                }
            }
            binding.bedroomChipGroup.addView(chip)
        }
    }

    private fun updateNumberOfPhotosChoiceOptions(selectedNumber: Int?) {
        binding.minPhotosCountChipGroup.removeAllViews()

        for (count in 2..MIN_PHOTO_COUNT_MAX_VALUE) {
            val chip = createChip(
                chipText = count.toString(), isFilterChip = false
            )
            chip.isChecked = (count == selectedNumber)
            chip.setOnCheckedChangeListener { _, isChecked ->
                filterViewModel.updatePhotosMinimalCount(
                    if (isChecked) {
                        count
                    } else 1
                )
            }
            binding.minPhotosCountChipGroup.addView(chip)
        }
    }

    private fun updateStatusChoiceOptions(currentChoice: EstateStatus?) {
        binding.statusChipGroup.removeAllViews()

        val options = resources.getStringArray(R.array.estate_status)
        for (status in options) {
            val chip = createChip(
                chipText = status.toString(), isFilterChip = false
            )
            chip.isChecked = (currentChoice == EstateStatus.valueOf(status.uppercase()))
            chip.setOnCheckedChangeListener { _, isChecked ->
                filterViewModel.updateEstateStatus(
                    if (isChecked) {
                        EstateStatus.valueOf(status.uppercase())
                    } else null
                )
            }
            binding.statusChipGroup.addView(chip)
        }
    }

    private fun updateEntryDateChoiceOptions(currentTimeframe: Timeframe?) {
        binding.entryDateChipGroup.removeAllViews()

        val options = Timeframe.entries
        for (timeframe in options) {
            val chip = createChip(
                chipText = timeframe.value, isFilterChip = false
            )
            chip.isChecked = (timeframe == currentTimeframe)
            chip.setOnCheckedChangeListener { _, isChecked ->

                filterViewModel.updateEntryDateTimeframe(
                    if (isChecked) {
                        timeframe
                    } else null
                )
            }
            binding.entryDateChipGroup.addView(chip)
        }
    }

    private fun updateSaleDateChoiceOptions(currentTimeframe: Timeframe?) {
        binding.saleDateChipGroup.removeAllViews()

        val options = Timeframe.entries
        for (timeframe in options) {
            val chip = createChip(
                chipText = timeframe.value, isFilterChip = false
            )
            chip.isChecked = (timeframe == currentTimeframe)
            chip.setOnCheckedChangeListener { _, isChecked ->
                filterViewModel.updateSaleDateTimeframe(
                    if (isChecked) {
                        timeframe
                    } else null
                )
            }
            binding.saleDateChipGroup.addView(chip)
        }
    }

    private fun updatePoiFilterOptions(selectedPoi: PointOfInterest?) {
        binding.poiChipGroup.removeAllViews()

        val poiList = PointOfInterest.entries

        for (poi in poiList) {
            val chip = createChip(
                chipText = poi.asUiPointOfInterest(requireContext()).name,
                iconResId = poi.asUiPointOfInterest(requireContext()).iconResId
            )
            chip.isChecked = (poi == selectedPoi)
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    filterViewModel.updatePointOfInterest(poi)
                } else {
                    filterViewModel.updatePointOfInterest(null)
                }
            }
            binding.poiChipGroup.addView(chip)
        }
    }

    private fun setUpCityAutocompleteTextView() {
        binding.citiesAutoCompleteTextView.setAdapter(availableStatesAdapter)

        binding.citiesAutoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    binding.textInputLayout.hint = null
                } else {
                    binding.textInputLayout.hint = getString(R.string.location_autocomplete_hint)
                }
            }
        })

        binding.citiesAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val city = availableStatesAdapter.getItem(position)
            city?.let { filterViewModel.addCityToSelection(it) }
            binding.citiesAutoCompleteTextView.setText(getString(R.string.empty_string))
        }
    }

    private fun updateSelectedCitiesChipGroup(selectedStates: List<String>) {
        binding.selectedCitiesChipGroup.removeAllViews()
        for (state in selectedStates) {
            val chip = createChip(chipText = state, isFilterChip = false)
            chip.isChecked = true
            chip.isCloseIconVisible = true

            chip.setOnCloseIconClickListener {
                filterViewModel.removeCityFromSelection(state)
            }

            binding.selectedCitiesChipGroup.addView(chip)
        }
    }

    private fun createChip(
        chipText: String,
        iconResId: Int? = null,
        isFilterChip: Boolean = true
    ): Chip {
        val chip = Chip(requireContext())
        chip.isCheckable = true
        chip.checkedIconTint = ContextCompat.getColorStateList(requireContext(), R.color.white)
        chip.chipBackgroundColor = ContextCompat.getColorStateList(
            requireContext(),
            R.color.chip_background_color_selector
        )
        chip.setTextColor(
            ContextCompat.getColorStateList(
                requireContext(),
                R.color.chip_text_color_selector
            )
        )
        chip.isCheckedIconVisible = isFilterChip

        chip.text = chipText
        iconResId?.let {
            chip.chipIcon = ContextCompat.getDrawable(
                requireContext(),
                it
            )
        }
        return chip
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}