package com.waminiyi.realestatemanager.features.editestate

import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import java.lang.NullPointerException
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
    val fullDescription: String = "",
    val fullDescriptionError: String? = null,
    val mainPhotoDescription: String = "",
    val mainPhotoDescriptionError: String? = null,
    val photos: List<Photo> = emptyList(),
    val photosError: String? = null,
    val address: Address? = null,
    val addressError: String? = null,
    val nearbyPointsOfInterest: List<PointOfInterest> = emptyList(),
    val estateStatus: EstateStatus = EstateStatus.AVAILABLE,
    val entryDate: Date? = null,
    val entryDateError: String? = null,
    val saleDate: Date? = null,
    val agent: Agent? = null,
    val agentError: String? = null,
    val isLoading: Boolean = false,
    val isEstateSaving: Boolean = false,
    val isEstateSaved: Boolean = false,
    val hasSavingError: Boolean = false,
    val savingError: String? = null,
    val hasChanges: Boolean = false
) {
    fun asEstateWithDetails(estateId: String?): EstateWithDetails {
        val mainPhoto = photos[0].copy(description = this.mainPhotoDescription, isMain = true)
        val photoMutableList = mutableListOf<Photo>()
        photoMutableList.addAll(this.photos)
        photoMutableList[0] = mainPhoto

        return EstateWithDetails(
            uuid = estateId ?: UUID.randomUUID().toString(),
            type = type ?: throw NullPointerException("Estate type can not be null"),
            price = price ?: throw NullPointerException("Estate price can not be null"),
            area = area ?: throw NullPointerException("Estate area can not be null"),
            roomsCount = roomsCount
                ?: throw NullPointerException("Estate rooms count can not be null"),
            fullDescription = fullDescription,
            photos = photoMutableList,
            address = address ?: throw NullPointerException("Address can not be null"),
            nearbyPointsOfInterest = nearbyPointsOfInterest,
            estateStatus = estateStatus ?: throw NullPointerException("Status can not be null"),
            entryDate = entryDate ?: throw NullPointerException("Entry date can not be null"),
            saleDate = saleDate,
            agent = agent ?: throw NullPointerException("Agent can not be null"),
        )
    }

    fun isNotDefaultState(): Boolean {
        return type != null ||
                price != null ||
                area != null ||
                roomsCount != null ||
                fullDescription.isNotBlank() ||
                mainPhotoDescription.isNotBlank() ||
                photos.isNotEmpty() ||
                nearbyPointsOfInterest.isNotEmpty() ||
                address != null ||
                entryDate != null ||
                agent != null
    }
}
