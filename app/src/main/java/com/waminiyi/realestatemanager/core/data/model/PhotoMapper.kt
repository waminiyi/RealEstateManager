package com.waminiyi.realestatemanager.core.data.model

import com.waminiyi.realestatemanager.core.data.remote.model.RemotePhoto
import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import java.util.UUID

fun RemotePhoto.toPhotoEntity() = PhotoEntity(
    photoUuid = UUID.fromString(this.uuid),
    estateUuid = UUID.fromString(this.estateUuid),
    url = this.url,
    localPath = "",
    isMainPhoto = this.isMainImage,
)

fun PhotoEntity.toRemotePhoto() = RemotePhoto(
    uuid = this.photoUuid.toString(),
    estateUuid = this.estateUuid.toString(),
    url = this.url,
    isMainImage = this.isMainPhoto
)