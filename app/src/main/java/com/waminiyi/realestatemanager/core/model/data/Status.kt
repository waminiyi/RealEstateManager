package com.waminiyi.realestatemanager.core.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Status {
    @SerialName("available")
    AVAILABLE,

    @SerialName("sold")
    SOLD,
}