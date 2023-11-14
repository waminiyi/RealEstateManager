package com.waminiyi.realestatemanager.core.database.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Class representing an estate with its associated main image .
 *
 * @property estateEntity The embedded estate entity containing estate details.
 * @property imageEntity The related image entity representing the main image associated with the estate.
 */
data class EstateWithImage(
    @Embedded
    val estateEntity: EstateEntity,

    @Relation(
        parentColumn = "main_image_id",
        entityColumn = "image_uuid"
    )
    val imageEntity: ImageEntity
)

