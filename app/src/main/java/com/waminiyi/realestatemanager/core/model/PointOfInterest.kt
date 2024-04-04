package com.waminiyi.realestatemanager.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enumeration class representing points of interest.
 */
@Serializable
enum class PointOfInterest {
    @SerialName("airport")
    AIRPORT,

    @SerialName("attraction")
    ATTRACTION,

    @SerialName("bank")
    BANK,

    @SerialName("fire_department")
    FIRE_DEPARTMENT,

    @SerialName("fitness_center")
    FITNESS_CENTER,

    @SerialName("hospital")
    HOSPITAL,

    @SerialName("park")
    PARK,

    @SerialName("pharmacy")
    PHARMACY,

    @SerialName("place_of_worship")
    PLACE_OF_WORSHIP,

    @SerialName("police_station")
    POLICE_STATION,

    @SerialName("post_office")
    POST_OFFICE,

    @SerialName("restaurant")
    RESTAURANT,

    @SerialName("school")
    SCHOOL,

    @SerialName("shopping_center")
    SHOPPING_CENTER,

    @SerialName("theater")
    THEATER,

    @SerialName("train_station")
    TRAIN_STATION,

    @SerialName("tram_station")
    TRAM_STATION
}