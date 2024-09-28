package com.waminiyi.realestatemanager.data.database.model

import androidx.room.Embedded
import com.waminiyi.realestatemanager.data.models.Address
import com.waminiyi.realestatemanager.data.models.Location

/**
 * Data class representing the address information for a real estate property.
 *
 * @property streetNumber The street number of the property.
 * @property streetName The name of the street where the property is located.
 * @property city The city where the property is located.
 * @property state The state or region where the property is located.
 * @property country The country where the property is located.
 * @property postalCode The postal code of the property's location.
 * @property location The geographic coordinates (latitude and longitude) of the property.
 */
data class AddressEntity(
    val streetNumber: Int?,
    val streetName: String?,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: Int?,
    @Embedded val location: Location
) {

    /**
     * Converts the AddressEntity object into an Address object.
     *
     * @return The converted Address object.
     */
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

/**
 * Converts the Address object into an AddressEntity object.
 *
 * @receiver The Address object to be converted.
 * @return The converted AddressEntity object.
 */
fun Address.asAddressEntity() = AddressEntity(
    streetNumber = this.streetNumber,
    streetName = this.streetName,
    city = this.city,
    state = this.state,
    country = this.country,
    postalCode = this.postalCode,
    location = this.location
)
