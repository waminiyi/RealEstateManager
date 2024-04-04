package com.waminiyi.realestatemanager.features.estateMapView

import com.waminiyi.realestatemanager.core.model.Estate

data class EstateMapUiState(
    val estates: List<Estate> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val hasFilter: Boolean = false,
    val isOnline: Boolean = true
)
