package com.waminiyi.realestatemanager.core.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FacilityType {
    @SerialName("bedroom")
    BEDROOM,
    @SerialName("bathroom")
    BATHROOM,
    @SerialName("living_room")
    LIVING_ROOM,
    @SerialName("kitchen")
    KITCHEN,
    @SerialName("dining_room")
    DINING_ROOM,
    @SerialName("garage")
    GARAGE,
    @SerialName("swimming_pool")
    SWIMMING_POOL,
    @SerialName("garden")
    GARDEN,
    @SerialName("balcony")
    BALCONY,
    @SerialName("terrace")
    TERRACE,
    @SerialName("parking_space")
    PARKING_SPACE,
    @SerialName("office")
    OFFICE,
    @SerialName("fitness_room")
    FITNESS_ROOM,
    @SerialName("library")
    LIBRARY,
    @SerialName("cinema_room")
    CINEMA_ROOM,
    @SerialName("spa")
    SPA,
    @SerialName("game_room")
    GAME_ROOM
}