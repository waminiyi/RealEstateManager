package com.waminiyi.realestatemanager.core.model.data

data class Address(
    val streetNumber: Int,
    val streetName: String,
    val city:String,
    val state:String,
    val postalCode: Int,
    val location: Location
)
