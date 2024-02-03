package com.waminiyi.realestatemanager.features.estatedetails

import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails

data class EstateDetailsUiState(
    val estateWithDetails: EstateWithDetails? = null,
    val isLoading: Boolean = false,
    val loadingError: String? = null,
)
