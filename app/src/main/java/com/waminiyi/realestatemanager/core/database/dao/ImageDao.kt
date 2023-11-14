package com.waminiyi.realestatemanager.core.database.dao

import androidx.annotation.VisibleForTesting
import androidx.room.*
import com.waminiyi.realestatemanager.core.database.model.ImageEntity
import java.util.*

@Dao
interface ImageDao {
    @Upsert
    suspend fun upsertImage(imageEntity: ImageEntity)

    @Delete
    suspend fun deleteImage(imageEntity: ImageEntity)

    @Query("SELECT * FROM images WHERE image_uuid = :imageUuid")
    fun getImageById(imageUuid: UUID): ImageEntity?

    @Query("SELECT * FROM images WHERE owner_uuid = :ownerUuid")
    fun getImagesByOwner(ownerUuid: UUID): List<ImageEntity>

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM images")
    fun getAllImages(): List<ImageEntity>
}
