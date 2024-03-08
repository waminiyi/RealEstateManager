package com.waminiyi.realestatemanager.features.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsStateWithCitiesAndZipCodes(
    @SerialName("state")
    val name: String,
    @SerialName("cityWithZipCodes")
    val cityWithZipCodes: List<CityWithZipCodes>
)