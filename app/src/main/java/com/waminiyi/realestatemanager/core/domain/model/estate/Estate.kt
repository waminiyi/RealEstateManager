package com.waminiyi.realestatemanager.core.domain.model.estate

data class Estate(
    val id: String, // Unique identifier for the property
    val type: EstateType, // Type of the property (e.g., Apartment, House, etc.)
    val price: Int, // Price of the property in dollars
    val area:Float,
    val mainImage: Image, // Main property image
    val facilities:List<Facility>,
    val address: Address, // Address of the property
    val status: Status, // Status of the property (Available, Sold, etc.)
)
