package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.DataResult

interface MediaFileRepository {

    suspend fun savePhotoFileToInternalStorage(inputUri: String, outputPhotoUuid: String) : String?
    suspend fun deletePhotoFileFromInternalStorage(photoUuid: String): Boolean
    suspend fun uploadPhoto(photoUri: String)
    suspend fun downloadPhoto(photoUrl: String)
    suspend fun deletePhoto(photoUrl: String)
}