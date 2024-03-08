package com.waminiyi.realestatemanager.features.estatesFilter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.data.repository.FilterRepository
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.NumberOfItems
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.Timeframe
import com.waminiyi.realestatemanager.core.util.remdispatchers.Dispatcher
import com.waminiyi.realestatemanager.core.util.remdispatchers.RemDispatchers
import com.waminiyi.realestatemanager.core.util.util.CitiesUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstateFilterViewModel @Inject constructor(
    @Dispatcher(RemDispatchers.IO) val ioDispatcher: CoroutineDispatcher,
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


    fun addEstateType(type: EstateType) {
        _uiState.update {
            val updatedFilter = it.filter.copy(estateTypes = it.filter.estateTypes + type)
            it.copy(filter = updatedFilter)
        }
    }

    fun removeEstateType(type: EstateType) {
        _uiState.update { uiState ->
            val updatedFilter =
                uiState.filter.copy(estateTypes = uiState.filter.estateTypes.filter { it != type })

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

    fun addRoomsCount(count: Int) {
        _uiState.update {
            val updatedFilter = it.filter.copy(roomsCounts = it.filter.roomsCounts + count)

            it.copy(filter = updatedFilter)
        }
    }

    fun removeRoomsCount(count: Int) {
        _uiState.update { state ->
            val updatedFilter =
                state.filter.copy(roomsCounts = state.filter.roomsCounts.filter { it != count })

            state.copy(filter = updatedFilter)
        }
    }

    fun addBedroomsCount(count: Int) {
        _uiState.update {
            val updatedFilter = it.filter.copy(bedroomsCounts = it.filter.bedroomsCounts + count)

            it.copy(filter = updatedFilter)
        }
    }

    fun removeBedroomsCount(count: Int) {
        _uiState.update { state ->

            val updatedFilter =
                state.filter.copy(bedroomsCounts = state.filter.bedroomsCounts.filter { it != count })

            state.copy(filter = updatedFilter)
        }
    }

    fun updatePhotosMinimalCount(count: Int) {
        _uiState.update {
            it.copy(filter = it.filter.copy(photosMinimalCount = count))
        }
    }


    fun addCityToSelection(city: String) {
        _uiState.update {
            val updatedFilter = it.filter.copy(cities = it.filter.cities + city)

            it.copy(filter = updatedFilter)
        }
    }

    fun removeCityFromSelection(city: String) {
        _uiState.update { state ->
            val updatedFilter =
                state.filter.copy(cities = state.filter.cities.filter { it != city })

            state.copy(filter = updatedFilter)
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
        filterRepository.filter.take(1).collect() { filter ->
            _uiState.update {
                it.copy(filter = filter, isLoadedUiState = true)
            }
        }
    }
}