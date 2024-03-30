package com.waminiyi.realestatemanager.features.estatesFilter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.data.repository.FilterRepository
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.Timeframe
import com.waminiyi.realestatemanager.core.util.util.CitiesUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstateFilterViewModel @Inject constructor(
    private val citiesUtils: CitiesUtils,
    private val filterRepository: FilterRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadFilter()
            val cities = citiesUtils.getUsCities()
            _uiState.update { it.copy(usCities = cities, isLoadedUiState = false) }
        }
    }

    fun updateEstateTypesFilter(type: EstateType, isChecked: Boolean) {
        _uiState.update { uiState ->
            val updatedFilter = if (isChecked) {
                uiState.filter.copy(estateTypes = uiState.filter.estateTypes + type)
            } else {
                uiState.filter.copy(estateTypes = uiState.filter.estateTypes.filter { it != type })
            }
            uiState.copy(filter = updatedFilter)
        }
    }

    fun updatePointOfInterest(poi: PointOfInterest?) {
        _uiState.update { it.copy(filter = it.filter.copy(pointOfInterest = poi)) }
    }

    fun updateMinPrice(price: Int?) {
        _uiState.update {
            it.copy(filter = it.filter.copy(minPrice = price))
        }
    }

    fun updateMaxPrice(price: Int?) {
        _uiState.update {
            it.copy(filter = it.filter.copy(maxPrice = price))
        }
    }

    fun updateMinArea(area: Int?) {
        _uiState.update {
            it.copy(filter = it.filter.copy(minArea = area))
        }
    }

    fun updateMaxArea(area: Int?) {
        _uiState.update {
            it.copy(filter = it.filter.copy(maxArea = area))
        }
    }

    fun updateRoomCountFilter(count: Int, isChecked: Boolean) {
        _uiState.update { uiState ->
            val updatedFilter = if (isChecked) {
                uiState.filter.copy(roomsCounts = uiState.filter.roomsCounts + count)
            } else {
                uiState.filter.copy(roomsCounts = uiState.filter.roomsCounts.filter { it != count })
            }
            uiState.copy(filter = updatedFilter)
        }
    }

    fun updateBedroomCountFilter(count: Int, isChecked: Boolean) {
        _uiState.update { uiState ->
            val updatedFilter = if (isChecked) {
                uiState.filter.copy(bedroomsCounts = uiState.filter.bedroomsCounts + count)
            } else {
                uiState.filter.copy(bedroomsCounts = uiState.filter.bedroomsCounts.filter { it != count })
            }
            uiState.copy(filter = updatedFilter)
        }
    }

    fun updatePhotosMinimalCount(count: Int) {
        _uiState.update {
            it.copy(filter = it.filter.copy(photosMinimalCount = count))
        }
    }

    fun updateSelectedCities(city: String, isAdded: Boolean) {
        _uiState.update { uiState ->
            val updatedFilter = if (isAdded) {
                if (uiState.filter.cities.contains(city)) return
                uiState.filter.copy(cities = uiState.filter.cities + city)
            } else {
                uiState.filter.copy(cities = uiState.filter.cities.filter { it != city })
            }
            uiState.copy(filter = updatedFilter)
        }
    }

    fun updateEstateStatus(status: EstateStatus?) {
        _uiState.update {
            it.copy(filter = it.filter.copy(estateStatus = status))
        }
    }

    fun updateEntryDateTimeframe(timeframe: Timeframe?) {
        _uiState.update {
            it.copy(filter = it.filter.copy(entryDateTimeframe = timeframe))
        }
    }

    fun updateSaleDateTimeframe(timeframe: Timeframe?) {
        _uiState.update {
            it.copy(filter = it.filter.copy(saleDateTimeframe = timeframe))
        }
    }

    fun saveFilter() {
        _uiState.update {
            filterRepository.updateFilter(it.filter)
            it.copy(isSaved = true)
        }
    }

    fun clearFilter() {
        _uiState.update {
            filterRepository.resetFilter()
            it.copy(isSaved = true)
        }
    }

    private suspend fun loadFilter() {
        filterRepository.filter.take(1).collect { filter ->
            _uiState.update {
                it.copy(filter = filter, isLoadedUiState = true)
            }
        }
    }
}