package com.waminiyi.realestatemanager.features.agentList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.data.repository.AgentRepository
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.features.estatesListView.EstateListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentListViewModel @Inject constructor(
    private val agentRepository: AgentRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgentListUiState())
    val uiState: StateFlow<AgentListUiState> = _uiState.asStateFlow()

    fun refreshAgents() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val agents = agentRepository.getAllAgents()) {
                is DataResult.Loading -> EstateListUiState(isLoading = true)
                is DataResult.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = agents.exception.message ?: "Error"
                    )
                }

                is DataResult.Success -> _uiState.update {
                    it.copy(isLoading = false, agents = agents.data)
                }
            }
        }
    }
}