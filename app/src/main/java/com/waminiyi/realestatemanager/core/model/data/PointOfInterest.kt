package com.waminiyi.realestatemanager.core.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PointOfInterest {
    SCHOOL,

    PUBLIC_TRANSPORT,

    GROCERY_STORE,

    @SerialName("park")
    PARK,

    @SerialName("medical_facility")
    MEDICAL_FACILITY,

    @SerialName("restaurant")
    RESTAURANT,

    @SerialName("shopping_center")
    SHOPPING_CENTER,

    @SerialName("fitness_center")
    FITNESS_CENTER,

    @SerialName("cultural_attraction")
    CULTURAL_ATTRACTION,

    @SerialName("place_of_worship")
    PLACE_OF_WORSHIP,

    @SerialName("bank")
    BANK,

    @SerialName("pharmacy")
    PHARMACY,

    @SerialName("police_station")
    POLICE_STATION,

    @SerialName("post_office")
    POST_OFFICE,

    @SerialName("gas_station")
    GAS_STATION,

    @SerialName("library")
    LIBRARY,

    @SerialName("movie_theater")
    MOVIE_THEATER,

    @SerialName("hospital")
    HOSPITAL,

    @SerialName("airport")
    AIRPORT,

    @SerialName("train_station")
    TRAIN_STATION
}