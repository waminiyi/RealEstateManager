package com.waminiyi.realestatemanager.core.data.datastore.repository

import com.waminiyi.realestatemanager.core.data.datastore.model.CachedUser
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    suspend fun updateDefaultCurrency(defaultCurrencyCode: CurrencyCode)
    suspend fun updateCurrentUserInfo(cachedUser: CachedUser)

    fun getDefaultCurrency(): Flow<CurrencyCode>
    fun getCurrentUserInfo(): Flow<CachedUser>
}