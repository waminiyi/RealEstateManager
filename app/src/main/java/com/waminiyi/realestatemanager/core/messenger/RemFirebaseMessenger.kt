package com.waminiyi.realestatemanager.core.messenger

import android.util.Log
import com.google.gson.Gson
import com.waminiyi.realestatemanager.core.messenger.model.RemMessage
import com.waminiyi.realestatemanager.core.messenger.network.FirebaseMessengerAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemFirebaseMessenger @Inject constructor(
    private val firebaseMessengerAPI: FirebaseMessengerAPI,
    private val accessTokenProvider: AccessTokenProvider,
) : RemMessenger {
    private val TAG = "RemFirebaseMessenger"
    private var cachedAccessToken: String? = null

    override fun postMessage(message: RemMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val accessToken = getCachedOrNewAccessToken()

                val response = firebaseMessengerAPI.postMessage(
                    authorization = "Bearer $accessToken",
                    message = message
                )
                if (response.isSuccessful) {
                    Log.d(TAG, " Successful Response: ${Gson().toJson(response)}")
                } else {
                    Log.e(TAG, response.errorBody()?.string().orEmpty())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun getCachedOrNewAccessToken(): String {
        if (cachedAccessToken.isNullOrEmpty()) {
            cachedAccessToken = accessTokenProvider.getServiceAccountAccessToken()
        }
        return cachedAccessToken.orEmpty()
    }
}