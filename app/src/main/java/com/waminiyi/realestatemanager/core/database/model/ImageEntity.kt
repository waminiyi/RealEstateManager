package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waminiyi.realestatemanager.core.model.data.Image
import com.waminiyi.realestatemanager.core.model.data.ImageType
import java.util.*

/**
 * Class representing an image in the real estate application.
 *
 * @property imageUuid The unique identifier (UUID) for the image. Automatically generated if not provided.
 * @property ownerUuid The unique identifier (UUID) of the owner associated with the image.
 * @property name The name of the image.
 * @property description The description of the image (can be null).
 * @property imageType The type of the image (e.g., main, additional, agent).
 */
@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    @ColumnInfo(name = "image_uuid")
    val imageUuid: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "owner_uuid")
    val ownerUuid: UUID,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "image_type")
    val imageType: ImageType
){
    fun asImage() = Image(
        uuid = this.imageUuid.toString(),
        name = this.name,
        description = this.description,
        imageType = this.imageType
    )
}
fun Image.asImageEntity(ownerId: String) = ImageEntity(
    imageUuid = UUID.fromString(this.uuid),
    ownerUuid = UUID.fromString(ownerId),
    name = this.name,
    description = this.description,
    imageType = this.imageType
)

