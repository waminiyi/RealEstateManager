package com.waminiyi.realestatemanager.features.estateMapView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.data.repository.FilterRepository
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.features.estatesListView.EstateListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TIME_OUT = 5000L

@HiltViewModel
class EstateMapViewModel @Inject constructor(
    private val estateRepository: EstateRepository,
    filterRepository: FilterRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = filterRepository.isDefaultFilter.flatMapLatest { isDefaultFilter ->
        estateRepository.getAllEstatesStream().map { estatesResult ->
            val uiState = when (estatesResult) {
                is DataResult.Error -> EstateMapUiState(
                    isError = true,
                    errorMessage = estatesResult.exception.message ?: "Error"
                )

                is DataResult.Loading -> EstateMapUiState(isLoading = true)
                is DataResult.Success -> EstateMapUiState(
                    estates = estatesResult.data,
                    hasFilter = !isDefaultFilter
                )
            }
            uiState
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIME_OUT),
        initialValue = EstateListUiState(isLoading = true)
    )
}