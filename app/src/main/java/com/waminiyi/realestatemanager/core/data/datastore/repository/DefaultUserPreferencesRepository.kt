package com.waminiyi.realestatemanager.core.data.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class DefaultUserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private val defaultCurrencyStream: Flow<CurrencyCode> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[DEFAULT_CURRENCY]?.let {
                CurrencyCode.valueOf(it)
            } ?: CurrencyCode.USD
        }

    private val estateListColumnCountStream: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[ESTATE_LIST_COLUMN_COUNT_KEY] ?: 1
        }

    override suspend fun updateDefaultCurrency(defaultCurrencyCode: CurrencyCode) {
        dataStore.edit { preferences ->
            preferences[DEFAULT_CURRENCY] = defaultCurrencyCode.name
        }
    }

    override suspend fun updateEstateListColumnCount(columnCount: Int) {
        dataStore.edit { preferences ->
            preferences[ESTATE_LIST_COLUMN_COUNT_KEY] = columnCount
        }
    }

    override fun getDefaultCurrency(): Flow<CurrencyCode> {
        return defaultCurrencyStream
    }

    override fun getEstateListColumnCount(): Flow<Int> {
        return estateListColumnCountStream
    }


    companion object PreferencesKeys {
        val DEFAULT_CURRENCY = stringPreferencesKey("default_currency")
        val ESTATE_LIST_COLUMN_COUNT_KEY = intPreferencesKey("estate_list_column_count")
    }
}