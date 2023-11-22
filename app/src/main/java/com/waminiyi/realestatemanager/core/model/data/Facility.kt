package com.waminiyi.realestatemanager.core.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Facility(
    @SerialName("facility_type")
    val type: FacilityType,
    @SerialName("facility_count")
    val count: Int
)
