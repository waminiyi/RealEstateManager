package com.waminiyi.realestatemanager.core.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EstateStatus {
    @SerialName("available")
    AVAILABLE,

    @SerialName("sold")
    SOLD,
}