package com.waminiyi.realestatemanager.core.model.data

import java.util.Date


/**
 * Data class representing an estate with additional details.
 * @property uuid The unique identifier of the estate.
 * @property type The type of the estate.
 * @property price The price of the estate.
 * @property area The area of the estate.
 * @property roomsCount The number of rooms in the estate (nullable).
 * @property bedroomsCount The number of bedrooms in the estate (nullable).
 * @property bathroomsCount The number of bathrooms in the estate (nullable).
 * @property fullDescription The full description of the estate (nullable).
 * @property photos The list of photos of the estate. Default is an empty list.
 * @property address The address of the estate.
 * @property nearbyPointsOfInterest The list of nearby points of interest. Default is an empty list.
 * @property estateStatus The status of the estate.
 * @property entryDate The entry date of the estate (nullable).
 * @property saleDate The sale date of the estate. Default is null.
 * @property agent The agent associated with the estate.
 */
data class EstateWithDetails(
    val uuid: String,
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
