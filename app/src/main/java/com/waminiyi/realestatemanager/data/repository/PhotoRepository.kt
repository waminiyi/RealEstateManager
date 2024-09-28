package com.waminiyi.realestatemanager.data.repository

import com.waminiyi.realestatemanager.data.models.Result
import com.waminiyi.realestatemanager.data.models.Photo

interface PhotoRepository  {
    suspend fun savePhoto(photo: Photo): Result<Unit>
    fun getAllEstatePhotos(estateUuid: String): Result<List<Photo>>
    fun getPhoto(photoUuid: String): Result<Photo>

    suspend fun deletePhoto(photo: Photo): Result<Unit>
    suspend fun deletePhoto(photoId: String): Result<Unit>
}