package com.waminiyi.realestatemanager.core.data.remote.model

import com.waminiyi.realestatemanager.core.database.model.AddressEntity
import com.waminiyi.realestatemanager.core.model.data.Location

data class RemoteAddress(
    val streetNumber: Int,
    val streetName: String,
    val city: String,
    val state: String,
    val postalCode: Int,
    val latitude: Double,
    val longitude: Double
)
