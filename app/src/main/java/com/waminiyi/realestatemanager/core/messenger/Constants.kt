package com.waminiyi.realestatemanager.core.messenger

import android.content.Context
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import java.io.IOException
import java.io.InputStream


object Constants {

    const val FIREBASE_MESSENGER_BASE_URL = "https://fcm.googleapis.com/v1/"
    const val STATIC_MAP_BASE_URL = "https://maps.googleapis.com"
    const val CONTENT_TYPE = "application/json"
    const val SCOPE = "https://www.googleapis.com/auth/firebase.messaging"
}

@Throws(IOException::class)
fun getServiceAccountAccessToken(context: Context): String? {
    // Use the application context to get the AssetManager
    val assetManager = context.applicationContext.assets

    // Open an InputStream for the service-account.json file in the assets directory
    val inputStream: InputStream = assetManager.open("service-account.json")

    // Use the InputStream to create GoogleCredentials
    val googleCredentials: GoogleCredentials = GoogleCredentials
        .fromStream(inputStream)
        .createScoped(listOf(Constants.SCOPE))

    // Refresh the credentials
    googleCredentials.refresh()

    Log.d("CREDENTIALS", googleCredentials.accessToken.tokenValue)
    // Return the access token
    return googleCredentials.accessToken.tokenValue
}