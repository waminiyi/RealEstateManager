package com.waminiyi.realestatemanager.features.agentList

import com.waminiyi.realestatemanager.core.model.data.Agent

data class AgentListUiState(
    val agents: List<Agent> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)
