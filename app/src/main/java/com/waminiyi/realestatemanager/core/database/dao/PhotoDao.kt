package com.waminiyi.realestatemanager.core.database.dao

import androidx.annotation.VisibleForTesting
import androidx.room.*
import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import com.waminiyi.realestatemanager.core.model.data.RegistrationStatus
import java.util.*

@Dao
interface PhotoDao {
    @Upsert
    suspend fun upsertPhoto(photoEntity: PhotoEntity)

    @Delete
    suspend fun deletePhoto(photoEntity: PhotoEntity)

    @Query("SELECT * FROM photos WHERE photo_uuid = :photoUuid")
    fun getPhotoById(photoUuid: UUID): PhotoEntity?

    @Query("SELECT * FROM photos WHERE estate_uuid = :estateUuid")
    fun getPhotosByEstate(estateUuid: UUID): List<PhotoEntity>

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM photos")
    fun getAllPhotos(): List<PhotoEntity>

    @Transaction
    @Query("SELECT * FROM photos WHERE upload_status = :status")
    suspend fun getPhotosWithUploadStatus(status: RegistrationStatus = RegistrationStatus.OnGoing): List<PhotoEntity>
}
