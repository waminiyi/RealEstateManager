package com.waminiyi.realestatemanager.core.map

import com.waminiyi.realestatemanager.core.messenger.Constants.STATIC_MAP_BASE_URL
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
object StaticMapModule {

    @Singleton
    @Provides
    fun provideStaticMapApiService(okHttpClient: OkHttpClient): StaticMapApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(STATIC_MAP_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(StaticMapApiService::class.java)
    }
}