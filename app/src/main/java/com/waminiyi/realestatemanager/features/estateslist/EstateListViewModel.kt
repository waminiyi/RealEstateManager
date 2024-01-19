package com.waminiyi.realestatemanager.features.estateslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.core.database.model.AddressEntity
import com.waminiyi.realestatemanager.core.database.model.EstateEntity
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.Location
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.features.agentEntities
import com.waminiyi.realestatemanager.features.estateEntities
import com.waminiyi.realestatemanager.features.mainPhotoEntities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EstateListViewModel @Inject constructor(
    private val estateRepository: EstateRepository,
    private val agentDao: AgentDao,
    private val photoDao: PhotoDao,
    private val estateDao: EstateDao
) : ViewModel() {

    init {
        addSampleEstates()
    }

    private val estates = estateRepository.getAllEstatesStream()

    val uiState = estates.map { estates ->
        when (estates) {
            is DataResult.Error -> EstateListUiState(
                isError = true,
                errorMessage = estates.exception.message ?: "Error"
            )

            is DataResult.Loading -> EstateListUiState(isLoading = true)
            is DataResult.Success -> EstateListUiState(estates = estates.data)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EstateListUiState(isLoading = true)
    )

    private fun addSampleEstates() {
        viewModelScope.launch {
            agentEntities.forEach { agentEntity ->
                agentDao.upsertAgent(agentEntity).also { Log.d("WRITETODB", it.toString()) }
            }
            estateEntities.forEach { estateEntity ->
                estateDao.upsertEstate(estateEntity).also { Log.d("WRITETODB", it.toString()) }
            }
            mainPhotoEntities.forEach { photoEntity ->
                photoDao.upsertPhoto(photoEntity).also { Log.d("WRITETODB", it.toString()) }
            }
        }
    }

    private fun getInitialEstates(): List<EstateEntity> {

        val address1 = AddressEntity(
            streetNumber = 123,
            streetName = "Main Street",
            city = "Cityville",
            state = "Stateville",
            postalCode = 12345,
            location = Location(latitude = 40.7128, longitude = -74.0060)
        )

        val address2 = AddressEntity(
            streetNumber = 456,
            streetName = "Broadway",
            city = "Metro City",
            state = "Stateville",
            postalCode = 67890,
            location = Location(latitude = 34.0522, longitude = -118.2437)
        )
//TODO (provide a list of uuid to avoid creating estates again and again)
        return listOf(
            EstateEntity(
                estateUuid = UUID.randomUUID(),
                type = EstateType.APARTMENT,
                price = 100000,
                area = 80.0f,
                roomsCount = 4,
                description = "Beautiful Apartment",
                addressEntity = address1,
                agentId = UUID.fromString("39bd0f42-d6a1-4968-8971-07b27b08ee95"),
                estateStatus = EstateStatus.AVAILABLE,
                entryDate = Date(System.currentTimeMillis())


            ),
            EstateEntity(
                estateUuid = UUID.randomUUID(),
                type = EstateType.HOUSE,
                price = 200000,
                area = 150.0f,
                roomsCount = 6,
                description = "Spacious House",
                addressEntity = address2,
                agentId = UUID.fromString("39bd0f42-d6a1-4968-8971-07b27b08ee95"),
                estateStatus = EstateStatus.AVAILABLE,
                entryDate = Date(System.currentTimeMillis())
            ),
        )
    }
}