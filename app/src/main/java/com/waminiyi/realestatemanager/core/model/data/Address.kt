package com.waminiyi.realestatemanager.core.model.data

data class Address(
    val streetNumber: Int?,
    val streetName: String?,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: Int?,
    val location: Location
)

fun Address.toRawString(): String {
    return "${streetNumber?.toString().orEmpty()} ${streetName?.let { "$it," }.orEmpty()} ${postalCode?.toString().orEmpty()} $city"
}
