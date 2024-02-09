package com.waminiyi.realestatemanager.features.estatedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.Constants
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.model.data.DataResult
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
) : ViewModel() {
    private var estateId: String? = savedStateHandle[Constants.ARG_ESTATE_ID]
    private val _uiState = MutableStateFlow(EstateDetailsUiState())
    val uiState: StateFlow<EstateDetailsUiState> = _uiState.asStateFlow()

    private fun loadEstate(estateId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = estateRepository.getEstateWithDetails(estateId)
            if (result !is DataResult.Success || result.data == null) {
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
        }
    }

    fun refreshEstateDetails() {
        estateId?.let { loadEstate(it) } ?: _uiState.update { it.copy(loadingError = "Null estate id") }
    }
}