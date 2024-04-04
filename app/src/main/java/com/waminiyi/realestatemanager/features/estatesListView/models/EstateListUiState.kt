package com.waminiyi.realestatemanager.features.estatesListView.models

import com.waminiyi.realestatemanager.core.model.Estate
import com.waminiyi.realestatemanager.core.utils.money.CurrencyCode

data class EstateListUiState(
    val estates: List<Estate> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val currencyCode: CurrencyCode = CurrencyCode.USD,
    val hasFilter: Boolean = false,
    val estateListColumnCount: Int = 1
)
