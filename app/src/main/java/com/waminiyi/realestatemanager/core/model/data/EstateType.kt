package com.waminiyi.realestatemanager.core.model.data

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EstateType {
    @SerialName("apartment")
    APARTMENT,

    @SerialName("house")
    HOUSE,

    @SerialName("villa")
    VILLA,

    @SerialName("cottage")
    COTTAGE,

    @SerialName("bungalow")
    BUNGALOW,

    @SerialName("chalet")
    CHALET,

    @SerialName("castle")
    CASTLE,

    @SerialName("other")
    OTHER,
}
