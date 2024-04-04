package com.waminiyi.realestatemanager.features.estateDetails

import com.waminiyi.realestatemanager.core.model.EstateWithDetails
import com.waminiyi.realestatemanager.core.utils.money.CurrencyCode

data class EstateDetailsUiState(
    val estateWithDetails: EstateWithDetails? = null,
    val isLoading: Boolean = false,
    val loadingError: String? = null,
    val currencyCode: CurrencyCode = CurrencyCode.USD
)
