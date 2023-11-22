package com.waminiyi.realestatemanager.core.data.model

import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import com.waminiyi.realestatemanager.core.data.remote.model.RemotePhoto
import java.util.*

fun RemotePhoto.toPhotoEntity() = PhotoEntity(
    imageUuid = UUID.fromString(this.uuid),
    estateUuid = UUID.fromString(this.estateUuid),
    url = this.url,
    localPath = "",
    description = this.description,
    isMainImage = this.isMainImage
)

fun PhotoEntity.toRemotePhoto() = RemotePhoto(
    uuid = this.imageUuid.toString(),
    estateUuid = this.estateUuid.toString(),
    url = this.url,
    description = this.description,
    isMainImage = this.isMainImage
)