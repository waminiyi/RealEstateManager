package com.waminiyi.realestatemanager.core.data.datastore.repository

import com.waminiyi.realestatemanager.core.data.datastore.model.CachedUser
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import com.waminiyi.realestatemanager.features.model.ListingViewType
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    suspend fun updateDefaultCurrency(defaultCurrencyCode: CurrencyCode)

    suspend fun updateEstateListColumnCount(columnCount: Int)

    fun getDefaultCurrency(): Flow<CurrencyCode>

    fun getEstateListColumnCount(): Flow<Int>
}