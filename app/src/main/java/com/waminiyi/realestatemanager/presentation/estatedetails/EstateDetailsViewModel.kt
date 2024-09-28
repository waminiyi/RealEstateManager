package com.waminiyi.realestatemanager.presentation.estatedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.Constants
import com.waminiyi.realestatemanager.data.datastore.repository.UserPreferencesRepository
import com.waminiyi.realestatemanager.data.repository.EstateRepository
import com.waminiyi.realestatemanager.data.models.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstateDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val estateRepository: EstateRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private var estateId: String? = savedStateHandle[Constants.ARG_ESTATE_ID]
    private val _uiState = MutableStateFlow(EstateDetailsUiState())
    val uiState: StateFlow<EstateDetailsUiState> = _uiState.asStateFlow()

    private fun loadEstate(estateId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {

            val result = estateRepository.getEstateWithDetails(estateId)
            if (result !is com.waminiyi.realestatemanager.data.models.DataResult.Result.Success || result.data == null) {
                _uiState.update { it.copy(isLoading = false, loadingError = "Estate with id $estateId not found") }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loadingError = null,
                        estateWithDetails = result.data
                    )
                }
            }
            userPreferencesRepository.getDefaultCurrency().collect { code ->
                _uiState.update { it.copy(currencyCode = code, isLoading = false) }
            }
        }
    }

    fun refreshEstateDetails() {
        estateId?.let { loadEstate(it) } ?: _uiState.update { it.copy(loadingError = "Null estate id") }
    }
}