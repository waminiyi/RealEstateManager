package com.waminiyi.realestatemanager.core.model.data

import java.util.Date


data class EstateWithDetails(
    val uuid: String, // TODO : change type to UUID
    val type: EstateType,
    val price: Int,
    val area: Int,
    val roomsCount: Int?,
    val bedroomsCount: Int?,
    val bathroomsCount: Int?,
    val fullDescription: String?,
    val photos: List<Photo> = emptyList(),
    val address: Address,
    val nearbyPointsOfInterest: List<PointOfInterest> = emptyList(),
    val estateStatus: EstateStatus,
    val entryDate: Date?,
    val saleDate: Date? = null,
    val agent: Agent
)
