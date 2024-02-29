package com.waminiyi.realestatemanager.features.agentDetails

import com.waminiyi.realestatemanager.core.model.data.Agent

data class AgentDetailsUiState(
    val agent: Agent? = null,
    val isLoading: Boolean = false,
    val loadingError: String? = null,
)
