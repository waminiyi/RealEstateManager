package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.DataResult
import com.waminiyi.realestatemanager.core.model.Photo

interface PhotoRepository  {
    suspend fun savePhoto(photo: Photo): DataResult<Unit>
    fun getAllEstatePhotos(estateUuid: String): DataResult<List<Photo>>
    fun getPhoto(photoUuid: String): DataResult<Photo>

    suspend fun deletePhoto(photo: Photo): DataResult<Unit>
    suspend fun deletePhoto(photoId: String): DataResult<Unit>
}