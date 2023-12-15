package com.waminiyi.realestatemanager.core.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultMediaFileRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : MediaFileRepository {
    override suspend fun savePhotoFileToInternalStorage(
        inputUri: String,
        outputPhotoUuid: String
    ): String? {
        val inputStream = context.contentResolver.openInputStream(inputUri.toUri())
        return inputStream?.use { input ->
            context.openFileOutput("$outputPhotoUuid.jpg", Context.MODE_PRIVATE).use { output ->
                input.copyTo(output)
            }
            getPhotoUriFromUuid(outputPhotoUuid, context).toString()
        }
    }

    override suspend fun deletePhotoFileFromInternalStorage(photoUuid: String): Boolean {
        val file = context.getFileStreamPath("$photoUuid.jpg")
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    override suspend fun uploadPhotoFile(photoUri: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun downloadPhotoFile(photoUrl: String): String? {
        TODO("Not yet implemented")
    }

    override suspend fun deletePhotoFromRemote(photoUrl: String) {
        TODO("Not yet implemented")
    }

    fun getPhotoUriFromUuid(uuid: String, context: Context): Uri? = context.getFileStreamPath("$uuid.jpg")?.let {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it)
    }
}