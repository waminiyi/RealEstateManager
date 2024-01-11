package com.waminiyi.realestatemanager.core.model.data

data class Estate(
    val uuid: String, // Unique identifier for the property
    val type: EstateType, // Type of the property (e.g., Apartment, House, etc.)
    val price: Int, // Price of the property in dollars
    val area: Float,
    val mainPhoto: Photo, // Main property image
    val addressCity: String, // Address of the property
)
