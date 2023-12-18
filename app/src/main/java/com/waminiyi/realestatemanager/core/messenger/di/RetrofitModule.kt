package com.waminiyi.realestatemanager.core.messenger.di

import com.waminiyi.realestatemanager.core.messenger.Constants.BASE_URL
import com.waminiyi.realestatemanager.core.messenger.network.FirebaseMessengerAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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