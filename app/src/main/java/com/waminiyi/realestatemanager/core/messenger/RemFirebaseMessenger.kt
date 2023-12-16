package com.waminiyi.realestatemanager.core.messenger

import android.util.Log
import com.google.gson.Gson
import com.waminiyi.realestatemanager.core.messenger.model.Message
import com.waminiyi.realestatemanager.core.messenger.network.FirebaseMessengerAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemFirebaseMessenger @Inject constructor(
    private val firebaseMessengerAPI: FirebaseMessengerAPI,
    private val accessTokenProvider: AccessTokenProvider
) : RemMessenger {
    private val TAG = "RemFirebaseMessenger"

    override fun postMessage(message: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = firebaseMessengerAPI.postMessage(
                    authorization = "Bearer " + accessTokenProvider.getServiceAccountAccessToken(),
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
}