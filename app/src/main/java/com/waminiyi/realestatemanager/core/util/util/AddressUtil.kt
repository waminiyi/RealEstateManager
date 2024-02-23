package com.waminiyi.realestatemanager.core.util.util

import com.google.android.libraries.places.api.model.AddressComponents
import com.google.android.libraries.places.api.model.Place
import com.waminiyi.realestatemanager.core.model.data.Address
import com.waminiyi.realestatemanager.core.model.data.Location


fun createAddressFromPlace(place: Place): Address {
    val addressComponents = place.addressComponents
    val streetNumber = getAddressComponent(addressComponents, "street_number")
    val streetName = getAddressComponent(addressComponents, "route")
    val city = getAddressComponent(addressComponents, "locality")
    val state = getAddressComponent(addressComponents, "administrative_area_level_1")
    val country = getAddressComponent(addressComponents, "country")
    val postalCode = getAddressComponent(addressComponents, "postal_code")
    val location = Location(place.latLng?.latitude ?: 0.0, place.latLng?.longitude ?: 0.0)

    return Address(streetNumber.toInt(), streetName, city, state, country, postalCode.toInt(), location)
}

private fun getAddressComponent(addressComponents: AddressComponents?, type: String): String {
    val component = addressComponents?.asList()?.find { it.types.contains(type) }
    return component?.name ?: ""
}