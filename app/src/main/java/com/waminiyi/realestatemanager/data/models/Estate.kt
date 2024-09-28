package com.waminiyi.realestatemanager.data.models

/**
 * Data class representing an estate.
 * @property uuid The unique identifier of the estate.
 * @property type The type of the estate.
 * @property price The price of the estate.
 * @property area The area of the estate.
 * @property mainPhoto The main photo of the estate.
 * @property addressCity The city where the estate is located.
 * @property location The location of the estate.
 * @property status The status of the estate.
 * @property roomsCount The number of rooms in the estate (nullable).
 */
data class Estate(
    val uuid: String,
    val type: EstateType,
    val price: Int,
    val area: Int,
    val mainPhoto: Photo,
    val addressCity: String,
    val location: Location,
    val status: EstateStatus,
    val roomsCount: Int?
)
