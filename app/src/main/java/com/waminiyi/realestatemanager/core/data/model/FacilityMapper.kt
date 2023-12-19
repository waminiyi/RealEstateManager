package com.waminiyi.realestatemanager.core.data.model

import com.waminiyi.realestatemanager.core.model.data.Facility
import com.waminiyi.realestatemanager.core.model.data.FacilityType
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteFacility

fun RemoteFacility.toFacility() = Facility(
    type = FacilityType.valueOf(this.type),
    count = this.count
)

fun Facility.toRemoteFacility() = RemoteFacility(
    type = this.type.name,
    count = this.count
)