package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.Photo
import com.waminiyi.realestatemanager.core.util.sync.Syncable

interface PhotoRepository : Syncable {
    suspend fun savePhoto(photo: Photo): DataResult<Unit>
    fun getAllEstatePhotos(estateUuid: String): DataResult<List<Photo>>
    fun getPhoto(photoUuid: String): DataResult<Photo>

    suspend fun deletePhoto(photo: Photo): DataResult<Unit>

    suspend  fun getPhotosToUpload(): List<PhotoEntity>
}