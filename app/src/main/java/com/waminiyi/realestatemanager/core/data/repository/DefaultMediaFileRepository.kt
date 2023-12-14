package com.waminiyi.realestatemanager.core.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.waminiyi.realestatemanager.core.database.dao.LocalChangeDao
import com.waminiyi.realestatemanager.core.database.model.LocalChangeEntity
import com.waminiyi.realestatemanager.core.model.data.ClassTag
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultMediaFileRepository @Inject constructor(
    private val localChangeDao: LocalChangeDao,
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
            localChangeDao.upsertChange(LocalChangeEntity(photoUuid, ClassTag.PhotoFile, true))
            file.delete()
        } else {
            false
        }
    }

    override suspend fun uploadPhoto(photoUri: String) {
        TODO("Not yet implemented")
    }

    override suspend fun downloadPhoto(photoUrl: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePhoto(photoUrl: String) {
        TODO("Not yet implemented")
    }

    fun getPhotoUriFromUuid(uuid: String, context: Context): Uri? = context.getFileStreamPath("$uuid.jpg")?.let {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it)
    }
}