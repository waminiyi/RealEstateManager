package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.UploadStatus
import java.util.UUID

/**
 * Class representing an image in the real estate application.
 *
 * @property photoUuid The unique identifier (UUID) for the image. Automatically generated if not provided.
 * @property estateUuid The unique identifier (UUID) of the owner associated with the image.
 * @property url The name of the image.
 * @property isMainPhoto Indicate whether it's the main image or not
 * @property localPath image local path
 */
@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = "photo_uuid")
    val photoUuid: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "estate_uuid")
    val estateUuid: UUID,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "local_path")
    val localPath: String,

    @ColumnInfo(name = "is_main_photo")
    val isMainPhoto: Boolean = false,

) {
    fun asPhoto(): Photo = Photo(
        uuid = this.photoUuid.toString(),
        estateUuid = this.estateUuid.toString(),
        remoteUrl = this.url,
        localPath = this.localPath,
        isMain = this.isMainPhoto
    )
}

fun Photo.asPhotoEntity(): PhotoEntity = PhotoEntity(
    photoUuid = UUID.fromString(this.uuid),
    estateUuid = UUID.fromString(this.estateUuid),
    url = this.remoteUrl ?: "",
    localPath = this.localPath ?: "",
    isMainPhoto = this.isMain
)