package com.waminiyi.realestatemanager.core.data.model

import com.waminiyi.realestatemanager.core.database.model.AddressEntity
import com.waminiyi.realestatemanager.core.model.data.Location
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteAddress

fun RemoteAddress.toAddressEntity() = AddressEntity(
    streetNumber = this.streetNumber,
    streetName = this.streetName,
    city = this.city,
    state = this.state,
    postalCode = this.postalCode,
    location = Location(
        this.latitude,
        this.longitude
    )
)

fun AddressEntity.toRemoteAddress() = RemoteAddress(
    streetNumber = this.streetNumber,
    streetName = this.streetName,
    city = this.city,
    state = this.state,
    postalCode = this.postalCode,
    latitude = this.location.latitude,
    longitude = this.location.longitude
)
