package com.waminiyi.realestatemanager.core.data.remote.model

import com.waminiyi.realestatemanager.core.database.model.AddressEntity
import com.waminiyi.realestatemanager.core.model.data.Location

data class RemoteAddress (
    val streetNumber: Int = 0,
    val streetName: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
