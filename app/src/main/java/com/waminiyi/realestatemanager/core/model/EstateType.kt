package com.waminiyi.realestatemanager.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enumeration class representing types of estates.
 */
@Serializable
enum class EstateType {
    @SerialName("apartment")
    APARTMENT,

    @SerialName("bungalow")
    BUNGALOW,

    @SerialName("castle")
    CASTLE,

    @SerialName("cottage")
    COTTAGE,

    @SerialName("chalet")
    CHALET,

    @SerialName("house")
    HOUSE,

    @SerialName("villa")
    VILLA,

    @SerialName("other")
    OTHER,
}
