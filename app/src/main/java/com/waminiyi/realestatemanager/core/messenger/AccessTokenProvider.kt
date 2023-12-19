package com.waminiyi.realestatemanager.core.messenger

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class AccessTokenProvider @Inject constructor(
    private val context: Context
) {
    @Throws(IOException::class)
    fun getServiceAccountAccessToken(): String? {
        val assetManager = context.assets
        val inputStream: InputStream = assetManager.open("service-account.json")

        val googleCredentials: GoogleCredentials = GoogleCredentials
            .fromStream(inputStream)
            .createScoped(listOf(Constants.SCOPE))

        googleCredentials.refresh()

        return googleCredentials.accessToken.tokenValue
    }
}