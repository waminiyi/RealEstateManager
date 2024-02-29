package com.waminiyi.realestatemanager.core.messenger.di

import com.waminiyi.realestatemanager.core.messenger.Constants.FIREBASE_MESSENGER_BASE_URL
import com.waminiyi.realestatemanager.core.messenger.network.FirebaseMessengerAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseMessengerApiModule {

    @Singleton
    @Provides
    fun provideFirebaseMessengerAPI(okHttpClient: OkHttpClient): FirebaseMessengerAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(FIREBASE_MESSENGER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(FirebaseMessengerAPI::class.java)
    }
}