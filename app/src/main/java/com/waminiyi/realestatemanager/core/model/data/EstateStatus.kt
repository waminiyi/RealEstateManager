package com.waminiyi.realestatemanager.core.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enumeration class representing the status of an estate.
 */
@Serializable
enum class EstateStatus {
    @SerialName("available")
    AVAILABLE,

    @SerialName("sold")
    SOLD,
}