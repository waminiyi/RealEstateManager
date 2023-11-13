package com.waminiyi.realestatemanager.features.estatelist

import com.waminiyi.realestatemanager.core.model.data.Estate

data class EstateListUiState(
    val estates: List<Estate> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
