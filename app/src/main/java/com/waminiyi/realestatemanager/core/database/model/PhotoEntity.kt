package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waminiyi.realestatemanager.core.model.data.Photo
import java.util.*

/**
 * Class representing an image in the real estate application.
 *
 * @property imageUuid The unique identifier (UUID) for the image. Automatically generated if not provided.
 * @property estateUuid The unique identifier (UUID) of the owner associated with the image.
 * @property url The name of the image.
 * @property description The description of the image (can be null).
 * @property isMainImage Indicate whether it's the main image or not
 */
@Entity(tableName = "images")
data class PhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = "image_uuid")
    val imageUuid: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "estate_uuid")
    val estateUuid: UUID,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "local_path")
    val localPath: String = "",

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "is_main_image")
    val isMainImage: Boolean = false
) {
    fun asPhoto(): Photo = if (isMainImage) {
        Photo.MainPhoto(
            uuid = this.imageUuid.toString(),
            description = this.description,
            url = this.url,
            localPath = this.localPath
        )
    } else {
        Photo.AdditionalPhoto(
            uuid = this.imageUuid.toString(),
            description = this.description,
            url = this.url,
        )
    }
}

fun Photo.asPhotoEntity(estateId: String): PhotoEntity {
    return when (this) {
        is Photo.MainPhoto -> PhotoEntity(
            imageUuid = UUID.fromString(this.uuid),
            estateUuid = UUID.fromString(estateId),
            url = this.url,
            localPath = this.localPath,
            description = this.description,
            isMainImage = true
        )

        is Photo.AdditionalPhoto -> PhotoEntity(
            imageUuid = UUID.fromString(this.uuid),
            estateUuid = UUID.fromString(estateId),
            url = this.url,
            localPath = "",
            description = this.description,
            isMainImage = false
        )
    }
}

