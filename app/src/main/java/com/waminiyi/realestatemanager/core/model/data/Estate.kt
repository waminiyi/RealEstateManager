package com.waminiyi.realestatemanager.core.model.data

data class Estate(
    val uuid: String,
    val type: EstateType,
    val price: Int,
    val area: Int,
    val mainPhoto: Photo,
    val addressCity: String,
    val location: Location,
    val status: EstateStatus,
    val roomsCount: Int?
)
