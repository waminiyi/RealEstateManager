package com.waminiyi.realestatemanager.core.data.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.waminiyi.realestatemanager.core.data.datastore.model.CachedUser
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import com.waminiyi.realestatemanager.features.model.ListingViewType
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

    private val cachedUserStream: Flow<CachedUser> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            CachedUser(
                id = preferences[CURRENT_USER_ID_KEY] ?: "",
                firstName = preferences[CURRENT_USER_FIRST_NAME_KEY] ?: "",
                lastName = preferences[CURRENT_USER_LAST_NAME_KEY] ?: "",
                photoUrl = preferences[CURRENT_USER_PHOTO_URL_KEY] ?: "",
                email = preferences[CURRENT_USER_EMAIL_KEY] ?: "",
                phoneNumber = preferences[CURRENT_USER_PHONE_KEY] ?: ""
            )
        }

    private val currentListingViewTypeStream: Flow<ListingViewType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[CURRENT_LISTING_VIEW_TYPE_KEY]?.let {
                ListingViewType.valueOf(it)
            } ?: ListingViewType.LIST
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

    override suspend fun updateCurrentViewType(viewType: ListingViewType) {
        dataStore.edit { preferences ->
            preferences[CURRENT_LISTING_VIEW_TYPE_KEY] = viewType.name
        }
    }


    override suspend fun updateCurrentUserInfo(cachedUser: CachedUser) {
        dataStore.edit { preferences ->
            preferences[CURRENT_USER_ID_KEY] = cachedUser.id
            preferences[CURRENT_USER_FIRST_NAME_KEY] = cachedUser.firstName
            preferences[CURRENT_USER_LAST_NAME_KEY] = cachedUser.lastName
            preferences[CURRENT_USER_PHOTO_URL_KEY] = cachedUser.photoUrl
            preferences[CURRENT_USER_EMAIL_KEY] = cachedUser.email
            preferences[CURRENT_USER_PHONE_KEY] = cachedUser.phoneNumber
        }
    }

    override fun getDefaultCurrency(): Flow<CurrencyCode> {
        return defaultCurrencyStream
    }

    override fun getEstateListColumnCount(): Flow<Int> {
        return estateListColumnCountStream
    }

    override fun getCurrentUserInfo(): Flow<CachedUser> {
        return cachedUserStream
    }

    override fun getCurrentViewType(): Flow<ListingViewType> {
        return currentListingViewTypeStream
    }


    companion object PreferencesKeys {
        val CURRENT_USER_FIRST_NAME_KEY = stringPreferencesKey("current_user_first_name")
        val CURRENT_USER_LAST_NAME_KEY = stringPreferencesKey("current_user_last_name")
        val CURRENT_USER_ID_KEY = stringPreferencesKey("current_user_id")
        val CURRENT_USER_PHOTO_URL_KEY = stringPreferencesKey("current_user_photo_url")
        val CURRENT_USER_EMAIL_KEY = stringPreferencesKey("current_user_photo_url")
        val CURRENT_USER_PHONE_KEY = stringPreferencesKey("current_user_photo_url")
        val DEFAULT_CURRENCY = stringPreferencesKey("default_currency")
        val CURRENT_LISTING_VIEW_TYPE_KEY = stringPreferencesKey("current_listing_view_type")
        val ESTATE_LIST_COLUMN_COUNT_KEY = intPreferencesKey("estate_list_column_count")
    }
}