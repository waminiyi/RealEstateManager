package com.waminiyi.realestatemanager.core.data.remote.model

data class RemoteAddress(
    val streetNumber: Int? = null,
    val streetName: String? = null,
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val postalCode: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
