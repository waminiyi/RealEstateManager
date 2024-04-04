package com.waminiyi.realestatemanager.features.home

import com.waminiyi.realestatemanager.core.utils.money.CurrencyCode
import com.waminiyi.realestatemanager.features.model.ListingViewType

data class HomeActivityUiState(
    val currencyCode: CurrencyCode = CurrencyCode.USD,
    val viewType: ListingViewType = ListingViewType.LIST,
    val hasFilter: Boolean = false,
    val estateCount: Int = 0,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isScreenSplittable: Boolean = false,
    val isScreenSplit: Boolean = false
)
