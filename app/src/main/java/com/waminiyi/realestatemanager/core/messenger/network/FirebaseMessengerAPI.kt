package com.waminiyi.realestatemanager.core.messenger.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface FirebaseMessengerAPI {
    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("projects/realestatemanager-3174d/messages:send")
    suspend fun postMessage(
        @Header("Authorization") authorization: String,
        @Body message: Any
    ): Response<ResponseBody>
}