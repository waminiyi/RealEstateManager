package com.waminiyi.realestatemanager.core.database.dao

import androidx.annotation.VisibleForTesting
import androidx.room.*
import com.waminiyi.realestatemanager.core.database.model.ImageEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ImageDao {
    @Upsert
    suspend fun upsertImage(imageEntity: ImageEntity)

    @Delete
    suspend fun deleteImage(imageEntity: ImageEntity)

    @Query("SELECT * FROM images WHERE image_uuid = :imageUuid")
    fun getImageById(imageUuid: UUID): Flow<ImageEntity?>

    @Query("SELECT * FROM images WHERE owner_uuid = :ownerUuid")
    fun getImagesByOwner(ownerUuid: UUID): Flow<List<ImageEntity>>

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM images")
    fun getAllImages(): Flow<List<ImageEntity>>
}
