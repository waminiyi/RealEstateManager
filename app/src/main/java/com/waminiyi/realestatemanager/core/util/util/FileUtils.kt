package com.waminiyi.realestatemanager.core.util.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FileUtils @Inject constructor(@ApplicationContext private val context: Context) {
    // Return the Uri of the saved file, Remember to replace ${context.packageName}.fileprovider
    // with the authority you specified in your FileProvider configuration.
    fun getPhotoUriFromUuid(uuid: String): Uri? = context.getFileStreamPath("$uuid.jpg")?.let {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", it)
    }
}