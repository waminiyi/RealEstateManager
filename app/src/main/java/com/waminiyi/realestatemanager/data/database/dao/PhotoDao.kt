package com.waminiyi.realestatemanager.data.database.dao

import android.database.Cursor
import androidx.annotation.VisibleForTesting
import androidx.room.*
import com.waminiyi.realestatemanager.data.database.model.PhotoEntity
import java.util.*

@Dao
interface PhotoDao {
    @Upsert
    suspend fun upsertPhoto(photoEntity: PhotoEntity)

    @Delete
    suspend fun deletePhoto(photoEntity: PhotoEntity)

    @Query("DELETE FROM photos WHERE photos.photo_uuid = :photoId")
    suspend fun deletePhoto(photoId: String)

    @Query("SELECT * FROM photos WHERE photo_uuid = :photoUuid")
    fun getPhotoById(photoUuid: UUID): PhotoEntity?

    @Query("SELECT * FROM photos WHERE estate_uuid = :estateUuid")
    fun getPhotosByEstate(estateUuid: UUID): List<PhotoEntity>

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM photos")
    fun getAllPhotos(): List<PhotoEntity>

    @Transaction
    @Query("SELECT * FROM photos WHERE photo_uuid IN (:photoUuids)")
    fun getPhotosByIds(photoUuids: List<UUID>): List<PhotoEntity>

    @Query("SELECT * FROM photos")
    fun getAllPhotosWithCursor(): Cursor?

    @Query("SELECT * FROM photos WHERE photo_uuid = :photoUuid")
    fun getPhotoWithCursorById(photoUuid: UUID): Cursor?
}
