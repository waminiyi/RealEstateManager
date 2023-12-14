package com.waminiyi.realestatemanager.core.data.model

import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import com.waminiyi.realestatemanager.core.data.remote.model.RemotePhoto
import com.waminiyi.realestatemanager.core.model.data.RegistrationStatus
import java.util.*

fun RemotePhoto.toPhotoEntity() = PhotoEntity(
    photoUuid = UUID.fromString(this.uuid),
    estateUuid = UUID.fromString(this.estateUuid),
    url = this.url,
    localPath = "",
    description = this.description,
    isMainPhoto = this.isMainImage,
    registrationStatus = RegistrationStatus.Finished
)

fun PhotoEntity.toRemotePhoto() = RemotePhoto(
    uuid = this.photoUuid.toString(),
    estateUuid = this.estateUuid.toString(),
    url = this.url,
    description = this.description,
    isMainImage = this.isMainPhoto
)