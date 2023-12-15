package com.waminiyi.realestatemanager.core.data.repository

interface MediaFileRepository {

    suspend fun savePhotoFileToInternalStorage(inputUri: String, outputPhotoUuid: String) : String?
    suspend fun deletePhotoFileFromInternalStorage(photoUuid: String): Boolean
    suspend fun uploadPhotoFile(photoUri: String): String?
    suspend fun downloadPhotoFile(photoUrl: String): String?
    suspend fun deletePhotoFromRemote(photoUrl: String)
}