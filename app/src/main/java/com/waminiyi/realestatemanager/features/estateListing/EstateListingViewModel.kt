package com.waminiyi.realestatemanager.features.estateListing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.data.datastore.repository.UserPreferencesRepository
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import com.waminiyi.realestatemanager.features.agentEntities
import com.waminiyi.realestatemanager.features.estateEntities
import com.waminiyi.realestatemanager.features.estatesListView.EstateListUiState
import com.waminiyi.realestatemanager.features.mainPhotoEntities
import com.waminiyi.realestatemanager.features.model.ListingViewType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EstateListingViewModel @Inject constructor(
    estateRepository: EstateRepository,
    private val agentDao: AgentDao,
    private val photoDao: PhotoDao,
    private val estateDao: EstateDao,
    private val userPreferencesRepository: UserPreferencesRepository

) : ViewModel() {

    private var currencyCode: CurrencyCode = CurrencyCode.USD
    private val currentViewTypeFlow = MutableStateFlow(ListingViewType.LIST)

    init {
        addSampleEstates()
    }

    private val combinedFlow = combine(
        estateRepository.getAllEstatesStream(),
        userPreferencesRepository.getDefaultCurrency(),
        currentViewTypeFlow
    ) { estatesResult, currency, viewType ->
        val uiState = when (estatesResult) {
            is DataResult.Error -> EstateListUiState(
                isError = true,
                errorMessage = estatesResult.exception.message ?: "Error"
            )

            is DataResult.Loading -> EstateListUiState(isLoading = true)
            is DataResult.Success -> EstateListUiState(
                estates = estatesResult.data,
                currencyCode = currency,
                viewType = viewType
            )
        }
        uiState
    }
    val uiState = combinedFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EstateListUiState(isLoading = true)
    )

    fun setCurrencyCode(code: CurrencyCode) {
        viewModelScope.launch {
            userPreferencesRepository.updateDefaultCurrency(code)
        }
    }

    fun setCurrentViewType(viewType: ListingViewType) {
        currentViewTypeFlow.value = viewType
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
                Log.d("photo", photoEntity.toString())
                photoDao.upsertPhoto(photoEntity)
            }
        }
    }
}