package com.waminiyi.realestatemanager.core.model.data

import java.util.*


data class EstateWithDetails(
    val uuid: String, // TODO : change type to UUID
    val type: EstateType,
    val price: Int, // Price of the property in dollars
    val area: Float,
    val roomsCount: Int,
    val fullDescription: String, // Full description of the property
    val photos: List<Photo> = emptyList(), // estate images
    val address: Address, // Address of the property
    val nearbyPointsOfInterest: List<PointOfInterest>, // Points of interest near the property
    val status: Status, // Status of the property (Available, Sold, etc.)
    val entryDate: Date, // Date when the property was listed
    val saleDate: Date? = null, // Date when the property was sold (can be null if not sold yet)
    val agent: Agent //Id of Agent responsible for the property
)
