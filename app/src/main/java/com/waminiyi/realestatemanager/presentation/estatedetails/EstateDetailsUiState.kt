package com.waminiyi.realestatemanager.presentation.estatedetails

import com.waminiyi.realestatemanager.data.models.EstateWithDetails
import com.waminiyi.realestatemanager.util.util.CurrencyCode

data class EstateDetailsUiState(
    val estateWithDetails: EstateWithDetails? = null,
    val isLoading: Boolean = false,
    val loadingError: String? = null,
    val currencyCode: CurrencyCode = CurrencyCode.USD
)
