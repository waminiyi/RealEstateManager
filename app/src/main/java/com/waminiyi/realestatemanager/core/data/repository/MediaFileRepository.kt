package com.waminiyi.realestatemanager.core.data.repository

import android.net.Uri

interface MediaFileRepository {

    suspend fun savePhotoFileToInternalStorage(inputUri: Uri, outputName: String): Uri?
    suspend fun deletePhotoFileFromInternalStorage(uri: String): Boolean

    fun getFilePathFromContentUri(contentUri: Uri): String?
}