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

    @SerialName("farmhouse")
    FARMHOUSE,

   @SerialName("condominium")
    CONDOMINIUM,

    @SerialName("townhouse")
    TOWNHOUSE,

    @SerialName("villa")
    VILLA,

    @SerialName("loft")
    LOFT,

    @SerialName("studio")
    STUDIO,

    @SerialName("duplex")
    DUPLEX,

    @SerialName("penthouse")
    PENTHOUSE,

    @SerialName("cottage")
    COTTAGE,

    @SerialName("ranch")
    RANCH,

    @SerialName("bungalow")
    BUNGALOW,

    @SerialName("chalet")
    CHALET,

    @SerialName("mobile_home")
    MOBILE_HOME,

    @SerialName("castle")
    CASTLE,

    @SerialName("mansion")
    MANSION,

    @SerialName("beach_house")
    BEACH_HOUSE,

    @SerialName("cabin")
    CABIN
}
