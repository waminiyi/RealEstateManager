package com.waminiyi.realestatemanager.core.data.repository

import android.content.Context
import android.net.Uri

interface MediaFileRepository {

    suspend fun savePhotoFileToInternalStorage(inputUri: Uri, outputName:String) : Uri?
    suspend fun deletePhotoFileFromInternalStorage(uri: String): Boolean
    suspend fun uploadPhotoFile(photoUri: String): String?
    suspend fun downloadPhotoFile(photoUrl: String): String?
    suspend fun deletePhotoFromRemote(photoUrl: String)

     fun  getFilePathFromContentUri(contentUri: Uri):String?
}