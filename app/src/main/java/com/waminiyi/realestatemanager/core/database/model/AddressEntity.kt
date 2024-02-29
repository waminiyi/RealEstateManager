package com.waminiyi.realestatemanager.core.database.model

import androidx.room.Embedded
import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Location

/**
 * Data class representing the address information for a real estate property.
 *
 * @property streetNumber The street number of the property.
 * @property streetName The name of the street where the property is located.
 * @property city The city where the property is located.
 * @property state The state or region where the property is located.
 * @property postalCode The postal code of the property's location.
 * @property location The geographic coordinates (latitude and longitude) of the property.
 */
data class AddressEntity(
    val streetNumber: Int?,
    val streetName: String?,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: Int,
    @Embedded val location: Location
) {
    fun asAddress() = Address(
        streetNumber = this.streetNumber,
        streetName = this.streetName,
        city = this.city,
        state = this.state,
        country = this.country,
        postalCode = this.postalCode,
        location = this.location
    )
}

fun Address.asAddressEntity() = AddressEntity(
    streetNumber = this.streetNumber,
    streetName = this.streetName,
    city = this.city,
    state = this.state,
    country = this.country,
    postalCode = this.postalCode,
    location = this.location
)
