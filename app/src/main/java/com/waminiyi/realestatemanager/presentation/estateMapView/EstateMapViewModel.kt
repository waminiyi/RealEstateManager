package com.waminiyi.realestatemanager.presentation.estateMapView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.data.repository.EstateRepository
import com.waminiyi.realestatemanager.data.repository.FilterRepository
import com.waminiyi.realestatemanager.data.models.Result
import com.waminiyi.realestatemanager.util.network.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TIME_OUT = 5000L

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class EstateMapViewModel @Inject constructor(
    private val estateRepository: EstateRepository,
    filterRepository: FilterRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = filterRepository.isDefaultFilter.flatMapLatest { isDefaultFilter ->
        combine(
            estateRepository.getAllEstatesStream(),
            networkMonitor.isOnline
        ) { estatesResult, isOnline ->
            val uiState = when (estatesResult) {
                is Result.Error -> EstateMapUiState(
                    isError = true,
                    errorMessage = estatesResult.exception.message ?: "Error"
                )

                is Result.Loading -> EstateMapUiState(isLoading = true)
                is Result.Success -> EstateMapUiState(
                    estates = estatesResult.data,
                    hasFilter = !isDefaultFilter
                )
            }
            uiState.copy(isOnline = isOnline)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIME_OUT),
        initialValue = EstateMapUiState(isLoading = true)
    )
}