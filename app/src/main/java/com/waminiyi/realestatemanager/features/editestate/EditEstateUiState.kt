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
    val type: EstateType? = null,
    val typeError: String? = null,
    val price: Int? = null,
    val priceError: String? = null,
    val area: Int? = null,
    val areaError: String? = null,
    val roomsCount: Int? = null,
    val roomsCountError: String? = null,
    val bedroomsCount: Int? = null,
    val bedroomsCountError: String? = null,
    val bathroomsCount: Int? = null,
    val bathroomsCountError: String? = null,
    val fullDescription: String = "",
    val fullDescriptionError: String? = null,
    val photos: List<Photo> = emptyList(),
    val photosError: String? = null,
    val address: Address? = null,
    val addressError: String? = null,
    val nearbyPointsOfInterest: List<PointOfInterest> = emptyList(),
    val estateStatus: EstateStatus = EstateStatus.AVAILABLE,
    val statusError: String? = null,
    val entryDate: Date? = null,
    val entryDateError: String? = null,
    val saleDate: Date? = null,
    val saleDateError: String? = null,
    val agent: Agent? = null,
    val agentError: String? = null,
    val isLoading: Boolean = false,
    val isEstateSaving: Boolean = false,
    val isEstateSaved: Boolean = false,
    val savingError: String? = null,
    val hasChanges: Boolean = false
) {
    fun asEstateWithDetails(estateId: String?): EstateWithDetails {

        return EstateWithDetails(
            uuid = estateId ?: UUID.randomUUID().toString(),
            type = type ?: throw NullPointerException("Estate type can not be null"),
            price = price ?: throw NullPointerException("Estate price can not be null"),
            area = area ?: throw NullPointerException("Estate area can not be null"),
            roomsCount = roomsCount
                ?: throw NullPointerException("Estate rooms count can not be null"),
            bedroomsCount = bedroomsCount
                ?: throw NullPointerException("Estate bedrooms count can not be null"),
            bathroomsCount = bathroomsCount
                ?: throw NullPointerException("Estate bathrooms count can not be null"),
            fullDescription = fullDescription,
            photos = photos,
            address = address ?: throw NullPointerException("Address can not be null"),
            nearbyPointsOfInterest = nearbyPointsOfInterest,
            estateStatus = estateStatus,
            entryDate = entryDate ?: throw NullPointerException("Entry date can not be null"),
            saleDate = saleDate,
            agent = agent ?: throw NullPointerException("Agent can not be null"),
        )
    }
}
