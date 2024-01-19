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
