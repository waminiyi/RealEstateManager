package com.waminiyi.realestatemanager.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.waminiyi.realestatemanager.core.model.data.Estate

/**
 * Class representing an estate with its associated main image .
 *
 * @property estateEntity The embedded estate entity containing estate details.
 * @property photoEntity The related image entity representing the main image associated with the estate.
 */
data class EstateWithImage(
    @Embedded
    val estateEntity: EstateEntity,

    @Relation(
        parentColumn = "main_image_id",
        entityColumn = "image_uuid"
    )
    val photoEntity: PhotoEntity
) {
    fun asEstate() = Estate(
        uuid = this.estateEntity.estateUuid.toString(),
        type = this.estateEntity.type,
        price = this.estateEntity.price,
        area = this.estateEntity.area,
        mainPhoto = this.photoEntity.asPhoto(),
        address = this.estateEntity.addressEntity.asAddress(),
        status = this.estateEntity.status
    )
}


