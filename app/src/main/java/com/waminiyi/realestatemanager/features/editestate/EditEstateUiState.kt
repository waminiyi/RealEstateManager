package com.waminiyi.realestatemanager.features.editestate

import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.Status
import java.lang.NullPointerException
import java.util.Date
import java.util.UUID

data class EditEstateUiState(
    val type: EstateType? = null,
    val price: Int = 0,
    val area: Float = 0F,
    val roomsCount: Int = 0,
    val fullDescription: String = "",
    val mainPhoto: Photo? = null,
    val mainPhotoDescription: String = "",
    val additionalPhotos: List<Photo> = emptyList(),
    val address: Address? = null,
    val nearbyPointsOfInterest: List<PointOfInterest> = emptyList(),
    val status: Status? = null,
    val entryDate: Date? = null,
    val saleDate: Date? = null,
    val agent: Agent? = null,
    val isLoading: Boolean = false,
    val isEstateSaving: Boolean = false,
    val isEstateSaved: Boolean = false,
    val savingError: String = ""
) {
    fun asEstateWithDetails(estateId: String?): EstateWithDetails {
        val mainPhoto = mainPhoto?.copy(description = this.mainPhotoDescription)
        val photos = mutableListOf<Photo>()

        mainPhoto?.let {
            photos.add(it)
        }
        photos.addAll(additionalPhotos)

        return EstateWithDetails(
            uuid = estateId ?: UUID.randomUUID().toString(),
            type = type ?: throw NullPointerException("Estate type can not be null"),
            price = price,
            area = area,
            roomsCount = roomsCount,
            fullDescription = fullDescription,
            photos = photos,
            address = address ?: throw NullPointerException("Address can not be null"),
            nearbyPointsOfInterest = nearbyPointsOfInterest,
            status = status ?: throw NullPointerException("Status can not be null"),
            entryDate = entryDate ?: throw NullPointerException("Entry date can not be null"),
            saleDate = saleDate,
            agent = agent ?: throw NullPointerException("Agent can not be null"),
        )
    }
}
