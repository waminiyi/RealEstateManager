package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.model.data.RegistrationStatus
import java.util.UUID

/**
 * Class representing an image in the real estate application.
 *
 * @property photoUuid The unique identifier (UUID) for the image. Automatically generated if not provided.
 * @property estateUuid The unique identifier (UUID) of the owner associated with the image.
 * @property url The name of the image.
 * @property description The description of the image (can be null).
 * @property isMainPhoto Indicate whether it's the main image or not
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
    val localPath: String = "",

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "is_main_photo")
    val isMainPhoto: Boolean = false,

    @ColumnInfo(name = "upload_status")
    val registrationStatus: RegistrationStatus = RegistrationStatus.OnGoing
) {
    fun asPhoto(): Photo = if (isMainPhoto) {
        Photo.MainPhoto(
            uuid = this.photoUuid.toString(),
            estateUuid = this.estateUuid.toString(),
            description = this.description,
            url = this.url,
            localPath = this.localPath
        )
    } else {
        Photo.AdditionalPhoto(
            uuid = this.photoUuid.toString(),
            estateUuid = this.estateUuid.toString(),
            description = this.description,
            url = this.url,
        )
    }
}

fun Photo.asPhotoEntity(): PhotoEntity {
    return when (this) {
        is Photo.MainPhoto -> PhotoEntity(
            photoUuid = UUID.fromString(this.uuid),
            estateUuid = UUID.fromString(this.estateUuid),
            url = this.url,
            localPath = this.localPath,
            description = this.description,
            isMainPhoto = true
        )

        is Photo.AdditionalPhoto -> PhotoEntity(
            photoUuid = UUID.fromString(this.uuid),
            estateUuid = UUID.fromString(this.estateUuid),
            url = this.url,
            localPath = "",
            description = this.description,
            isMainPhoto = false
        )
    }
}

