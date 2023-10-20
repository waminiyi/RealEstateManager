package com.waminiyi.realestatemanager.core.domain.model.estate

import java.util.Date

data class EstateWithDetails(
    val id: String, // Unique identifier for the property
    val type: EstateType, // Type of the property (e.g., Apartment, House, etc.)
    val price: Int, // Price of the property in dollars
    val area:Float,
    val facilities:List<Facility>,
    val description: String, // Full description of the property
    val mainImage: Image, // Main property image
    val additionalImages: List<Image> = emptyList(), // Optional additional property images
    val address: Address, // Address of the property
    val nearbyPointsOfInterest: List<PointOfInterest>, // Points of interest near the property
    val status: Status, // Status of the property (Available, Sold, etc.)
    val entryDate: Date, // Date when the property was listed
    val saleDate: Date? = null, // Date when the property was sold (can be null if not sold yet)
    val agentId: String //Id of Agent responsible for the property
)
