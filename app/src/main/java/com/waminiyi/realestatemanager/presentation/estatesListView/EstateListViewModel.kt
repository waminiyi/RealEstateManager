package com.waminiyi.realestatemanager.presentation.estatesListView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.data.datastore.repository.UserPreferencesRepository
import com.waminiyi.realestatemanager.data.repository.EstateRepository
import com.waminiyi.realestatemanager.data.repository.FilterRepository
import com.waminiyi.realestatemanager.data.models.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TIME_OUT = 5000L

@HiltViewModel
class EstateListViewModel @Inject constructor(
    private val estateRepository: EstateRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    filterRepository: FilterRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val combinedFlow = filterRepository.isDefaultFilter.flatMapLatest { isDefaultFilter ->
        combine(
            estateRepository.getAllEstatesStream(),
            userPreferencesRepository.getDefaultCurrency(),
            userPreferencesRepository.getEstateListColumnCount()
        ) { estatesResult, currency, listColumnCount ->
            val uiState = when (estatesResult) {
                is Result.Error -> EstateListUiState(
                    isError = true,
                    errorMessage = estatesResult.exception.message ?: "Error"
                )

                is Result.Loading -> EstateListUiState(isLoading = true)
                is Result.Success -> EstateListUiState(
                    estates = estatesResult.data,
                    currencyCode = currency,
                    hasFilter = !isDefaultFilter,
                    estateListColumnCount = listColumnCount
                )
            }
            uiState
        }
    }

    val uiState = combinedFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIME_OUT),
        initialValue = EstateListUiState(isLoading = true)
    )
}