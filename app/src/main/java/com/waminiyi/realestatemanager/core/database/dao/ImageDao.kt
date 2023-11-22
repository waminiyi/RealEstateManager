package com.waminiyi.realestatemanager.core.database.dao

import androidx.annotation.VisibleForTesting
import androidx.room.*
import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import java.util.*

@Dao
interface ImageDao {
    @Upsert
    suspend fun upsertImage(photoEntity: PhotoEntity)

    @Delete
    suspend fun deleteImage(photoEntity: PhotoEntity)

    @Query("SELECT * FROM images WHERE image_uuid = :imageUuid")
    fun getImageById(imageUuid: UUID): PhotoEntity?

    @Query("SELECT * FROM images WHERE estate_uuid = :estateUuid")
    fun getImagesByEstate(estateUuid: UUID): List<PhotoEntity>

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM images")
    fun getAllImages(): List<PhotoEntity>
}
