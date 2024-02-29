package com.waminiyi.realestatemanager.features.agentDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.Constants
import com.waminiyi.realestatemanager.core.data.repository.AgentRepository
import com.waminiyi.realestatemanager.core.model.data.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val agentRepository: AgentRepository,
) : ViewModel() {
    private var agentId: String? = savedStateHandle[Constants.ARG_AGENT_ID]
    private val _uiState = MutableStateFlow(AgentDetailsUiState())
    val uiState: StateFlow<AgentDetailsUiState> = _uiState.asStateFlow()

    private fun loadAgentDetails(agentId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = agentRepository.getAgent(agentId)
            if (result !is DataResult.Success || result.data == null) {
                _uiState.update { it.copy(isLoading = false, loadingError = "Agent with id $agentId not found") }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loadingError = null,
                        agent = result.data
                    )
                }
            }
        }
    }

    fun refreshAgentDetails() {
        agentId?.let { loadAgentDetails(it) } ?: _uiState.update { it.copy(loadingError = "Null estate id") }
    }
}