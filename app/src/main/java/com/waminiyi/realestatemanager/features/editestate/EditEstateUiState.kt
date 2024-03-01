package com.waminiyi.realestatemanager.features.editestate

import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import java.util.Date
import java.util.UUID

data class EditEstateUiState(
    val type: EstateType = EstateType.APARTMENT,
    val typeError: String? = null,
    val price: Int? = null,
    val priceError: String? = null,
    val area: Int? = null,
    val areaError: String? = null,
    val roomsCount: Int? = null,
    val roomsCountError: String? = null,
    val bedroomsCount: Int? = null,
    val bathroomsCount: Int? = null,
    val fullDescription: String? = null,
    val photos: List<Photo> = emptyList(),
    val photosError: String? = null,
    val address: Address? = null,
    val addressError: String? = null,
    val nearbyPointsOfInterest: List<PointOfInterest> = emptyList(),
    val estateStatus: EstateStatus = EstateStatus.AVAILABLE,
    val statusError: String? = null,
    val entryDate: Date? = null,
    val dateError: String? = null,
    val saleDate: Date? = null,
    val agent: Agent? = null,
    val agentsList: List<Agent> = emptyList(),
    val agentError: String? = null,
    val isLoading: Boolean = false,
    val isEstateSaving: Boolean = false,
    val isEstateSaved: Boolean = false,
    val savingError: String? = null,
    val hasChanges: Boolean = false
) {
    fun asEstateWithDetails(estateId: String?): EstateWithDetails {

        val tempPhotos = mutableListOf<Photo>()
        tempPhotos.addAll(photos)
        tempPhotos[0] = tempPhotos[0].copy(isMain = true)
        return EstateWithDetails(
            uuid = estateId ?: UUID.randomUUID().toString(),
            type = type,
            price = price ?: throw NullPointerException("Estate price can not be null"),
            area = area ?: throw NullPointerException("Estate area can not be null"),
            roomsCount = roomsCount,
            bedroomsCount = bedroomsCount,
            bathroomsCount = bathroomsCount,
            fullDescription = fullDescription,
            photos = tempPhotos,
            address = address ?: throw NullPointerException("Address can not be null"),
            nearbyPointsOfInterest = nearbyPointsOfInterest,
            estateStatus = estateStatus,
            entryDate = entryDate,
            saleDate = saleDate,
            agent = agent ?: throw NullPointerException("Agent can not be null"),
        )
    }
}
