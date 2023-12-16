package com.waminiyi.realestatemanager.core.messenger.di

import android.content.Context
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.waminiyi.realestatemanager.core.messenger.Constants
import com.waminiyi.realestatemanager.core.messenger.Constants.BASE_URL
import com.waminiyi.realestatemanager.core.messenger.network.FirebaseMessengerAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideFirebaseMessengerAPI(retrofit: Retrofit): FirebaseMessengerAPI {
        return retrofit.create(FirebaseMessengerAPI::class.java)
    }
}