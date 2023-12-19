package com.waminiyi.realestatemanager.core.messenger.di

import com.waminiyi.realestatemanager.core.messenger.Constants.BASE_URL
import com.waminiyi.realestatemanager.core.messenger.network.FirebaseMessengerAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Singleton
class RetrofitInstance {
    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api by lazy {
            retrofit.create(FirebaseMessengerAPI::class.java)
        }
    }
}