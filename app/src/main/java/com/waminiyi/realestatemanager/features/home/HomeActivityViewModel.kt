package com.waminiyi.realestatemanager.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.data.datastore.repository.UserPreferencesRepository
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.data.repository.FilterRepository
import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import com.waminiyi.realestatemanager.features.agentEntities
import com.waminiyi.realestatemanager.features.estateEntities
import com.waminiyi.realestatemanager.features.mainPhotoEntities
import com.waminiyi.realestatemanager.features.model.ListingViewType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TIME_OUT = 5000L


@HiltViewModel
class HomeActivityViewModel @Inject constructor(
    estateRepository: EstateRepository,
    private val agentDao: AgentDao,
    private val photoDao: PhotoDao,
    private val estateDao: EstateDao,
    private val userPreferencesRepository: UserPreferencesRepository,
    filterRepository: FilterRepository
) : ViewModel() {

    init {
        addSampleEstates()
    }

    val uiState: StateFlow<HomeActivityUiState> = combine(
        estateRepository.getAllEstatesStream(),
        filterRepository.isDefaultFilter,
        userPreferencesRepository.getDefaultCurrency(),
        userPreferencesRepository.getCurrentViewType()
    ) { estatesResult, isDefaultFilter, currency, viewType ->
        HomeActivityUiState(
            currencyCode = currency,
            viewType = viewType,
            hasFilter = !isDefaultFilter,
            estateCount = when (estatesResult) {
                is DataResult.Success -> estatesResult.data.size
                else -> 0
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIME_OUT),
        initialValue = HomeActivityUiState(isLoading = true)
    )

    fun updateEstateListColumnCount(columnCount: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateEstateListColumnCount(columnCount)
        }
    }

    fun updateCurrencyCode(code: CurrencyCode) {
        viewModelScope.launch {
            userPreferencesRepository.updateDefaultCurrency(code)
        }
    }

    fun updateCurrentViewType(viewType: ListingViewType) {
        viewModelScope.launch {
            userPreferencesRepository.updateCurrentViewType(viewType)
        }
    }

    private fun addSampleEstates() {
        viewModelScope.launch {
            agentEntities.forEach { agentEntity ->
                agentDao.upsertAgent(agentEntity)
            }
            estateEntities.forEach { estateEntity ->
                estateDao.upsertEstate(estateEntity)
            }
            mainPhotoEntities.forEach { photoEntity ->
                photoDao.upsertPhoto(photoEntity)
            }
        }
    }
}