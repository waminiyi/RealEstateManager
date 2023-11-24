package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.model.data.Photo

interface PhotoRepository {
    suspend fun savePhoto(photo: Photo): DataResult<Unit>
    fun getAllEstatePhotos(estateUuid: String): DataResult<List<Photo>>
    fun getPhoto(photoUuid: String): DataResult<Photo?>

    fun deletePhoto(photo: Photo): DataResult<Unit>
}