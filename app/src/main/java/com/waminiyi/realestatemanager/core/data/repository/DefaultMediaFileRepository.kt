package com.waminiyi.realestatemanager.core.data.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

class DefaultMediaFileRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : MediaFileRepository {
    override suspend fun savePhotoFileToInternalStorage(
        inputUri: Uri, outputName: String
    ): Uri? {
        val fileName = "IMG_${outputName}"
        val inputStream = context.contentResolver.openInputStream(inputUri)
        return inputStream?.use { input ->
            context.openFileOutput("$fileName.jpg", Context.MODE_PRIVATE).use { output ->
                input.copyTo(output)
                output.close()
            }
            inputStream.close()
            getUriFromName("$fileName.jpg", context)
        }
    }

    override suspend fun deletePhotoFileFromInternalStorage(uri: String): Boolean {

        if (context.deleteFileByUri(uri.toUri())) {
            // File deleted successfully
            Log.d("FileDeletion", "File deleted: ${uri.toUri()}")
            return true
        } else {
            // Failed to delete the file
            Log.e("FileDeletion", "Failed to delete file: ${uri.toUri()}")
            return false
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

    override fun getFilePathFromContentUri(contentUri: Uri): String? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(contentUri, filePathColumn, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(filePathColumn[0])
                return it.getString(columnIndex)
            }
        }

        return null
    }

    fun getUriFromName(name: String, context: Context): Uri? {
        val file = context.getFileStreamPath(name)

        if (file != null && file.exists()) {
            // File exists, log its absolute path
            Log.d("InternalStorage", "File exists: ${file.absolutePath}")

            // Create a content URI for the file
            val uri = Uri.fromFile(file)

            Log.d("InternalStorage", "Generated URI: $uri")

            return uri
        } else {
            // File does not exist or is null
            Log.e("InternalStorage", "File does not exist or is null: $name")
            return null
        }
    }

    fun Context.deleteFileByUri(fileUri: Uri): Boolean {
        try {
            val contentResolver = this.contentResolver
            val deletedRows = contentResolver.delete(fileUri, null, null)
            return deletedRows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


}