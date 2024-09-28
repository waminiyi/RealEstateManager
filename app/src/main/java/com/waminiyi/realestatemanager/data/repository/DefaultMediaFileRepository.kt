package com.waminiyi.realestatemanager.data.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
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

        return if (context.deleteFileByUri(uri.toUri())) {
            true
        } else {
            Log.e("FileDeletion", "Failed to delete file: ${uri.toUri()}")
            false
        }
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

    private fun getUriFromName(name: String, context: Context): Uri? {
        val file = context.getFileStreamPath(name)

        return if (file != null && file.exists()) {
            val uri = Uri.fromFile(file)
            uri
        } else {
            Log.e("InternalStorage", "File does not exist or is null: $name")
            null
        }
    }

    private fun Context.deleteFileByUri(fileUri: Uri): Boolean {
        return try {
            val contentResolver = this.contentResolver
            val deletedRows = contentResolver.delete(fileUri, null, null)
            deletedRows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


}