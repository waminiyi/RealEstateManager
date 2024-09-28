package com.waminiyi.realestatemanager.data.models

/**
 * Data class representing an address.
 * @property streetNumber The street number.
 * @property streetName The street name.
 * @property city The city.
 * @property state The state.
 * @property country The country.
 * @property postalCode The postal code.
 * @property location The location.
 */
data class Address(
    val streetNumber: Int?,
    val streetName: String?,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: Int?,
    val location: Location
)

/**
 * Converts the Address object to a raw string representation.
 * @return The raw string representation of the Address.
 */
fun Address.toRawString(): String {
    return "${streetNumber?.toString().orEmpty()} " +
            streetName?.let { "$it," }.orEmpty() +
            " ${postalCode?.toString().orEmpty()} $city"
}
