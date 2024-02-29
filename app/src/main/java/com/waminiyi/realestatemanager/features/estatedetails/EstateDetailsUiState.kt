package com.waminiyi.realestatemanager.features.estatedetails

import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode

data class EstateDetailsUiState(
    val estateWithDetails: EstateWithDetails? = null,
    val isLoading: Boolean = false,
    val loadingError: String? = null,
    val currencyCode: CurrencyCode = CurrencyCode.USD
)
