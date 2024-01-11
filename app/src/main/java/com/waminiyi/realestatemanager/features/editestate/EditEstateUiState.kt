package com.waminiyi.realestatemanager.features.editestate

import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.Status
import java.util.Date

data class EditEstateUiState(
    val uuid: String = "",
    val type: EstateType = EstateType.APARTMENT,
    val price: Int = 0,
    val area: Float = 0F,
    val roomsCount: Int = 0,
    val fullDescription: String = "Add a description",
    val mainPhoto: Photo? = null,
    val mainPhotoDescription: String = " About main photo",
    val additionalPhotos: List<Photo> = emptyList(),
    val address: Address? = null,
    val nearbyPointsOfInterest: List<PointOfInterest> = emptyList(),
    val status: Status? = null,
    val entryDate: Date? = null,
    val saleDate: Date? = null,
    val agent: Agent? = null,
    val isLoading: Boolean = false,
    val isEstateSaved: Boolean = false
)
