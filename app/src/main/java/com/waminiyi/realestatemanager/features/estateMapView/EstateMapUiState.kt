package com.waminiyi.realestatemanager.features.estateMapView

import com.waminiyi.realestatemanager.core.model.data.Estate
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import com.waminiyi.realestatemanager.features.model.ListingViewType

data class EstateMapUiState(
    val estates: List<Estate> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val hasFilter: Boolean = false
)
