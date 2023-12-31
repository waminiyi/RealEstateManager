package com.waminiyi.realestatemanager.core.data.model

import com.waminiyi.realestatemanager.core.data.remote.model.RemoteEstate
import com.waminiyi.realestatemanager.core.database.model.EstateEntity
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import com.waminiyi.realestatemanager.core.model.data.Status
import java.util.Date
import java.util.UUID

fun RemoteEstate.toEstateEntity() = EstateEntity(
    estateUuid = UUID.fromString(this.estateUuid),
    type = EstateType.valueOf(this.type),
    price = this.price,
    area = this.area,
    description = this.description,
    addressEntity = this.address.toAddressEntity(),
    status = Status.valueOf(this.status),
    entryDate = Date(this.entryDate),
    saleDate = this.saleDate?.let { Date(it) },
    agentId = UUID.fromString(this.agentId),
    poiList = this.poiList.map { PointOfInterest.valueOf(it) },
    facilitiesList = this.facilitiesList.map { it.toFacility() },
)

fun EstateEntity.toRemoteEstate() = RemoteEstate(
    estateUuid = this.estateUuid.toString(),
    type = this.type.name,
    price = this.price,
    area = this.area,
    description = this.description,
    address = this.addressEntity.toRemoteAddress(),
    status = this.status.name,
    entryDate = this.entryDate.time,
    saleDate = this.saleDate?.time,
    agentId = this.agentId.toString(),
    poiList = this.poiList.map { it.name },
    facilitiesList = this.facilitiesList.map { it.toRemoteFacility() }
)