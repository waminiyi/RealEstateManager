package com.waminiyi.realestatemanager.presentation.estatesListView

import com.waminiyi.realestatemanager.data.models.Estate
import com.waminiyi.realestatemanager.util.util.CurrencyCode

data class EstateListUiState(
    val estates: List<Estate> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val currencyCode: CurrencyCode = CurrencyCode.USD,
    val hasFilter: Boolean = false,
    val estateListColumnCount: Int = 1
)
