package com.waminiyi.realestatemanager.features.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityWithZipCodes(
    @SerialName("city")
    val cityName: String,
    @SerialName("zipCodes")
    val zipCodes: List<String>
)