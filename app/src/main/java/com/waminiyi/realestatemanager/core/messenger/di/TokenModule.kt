package com.waminiyi.realestatemanager.core.messenger.di

import android.content.Context
import com.waminiyi.realestatemanager.core.messenger.AccessTokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokenModule {

    @Provides
    @Singleton
    fun provideAccessTokenProvider(@ApplicationContext context: Context): AccessTokenProvider {
        return AccessTokenProvider(context)
    }
}