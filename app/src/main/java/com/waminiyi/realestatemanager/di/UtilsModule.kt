package com.waminiyi.realestatemanager.di

import android.content.Context
import com.waminiyi.realestatemanager.util.remdispatchers.Dispatcher
import com.waminiyi.realestatemanager.util.remdispatchers.RemDispatchers
import com.waminiyi.realestatemanager.util.util.CitiesUtils
import com.waminiyi.realestatemanager.util.util.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Singleton
    @Provides
    fun provideNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }

    @Singleton
    @Provides
    fun provideCitiesUtils(
        @ApplicationContext context: Context,
        @Dispatcher(RemDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): CitiesUtils {
        return CitiesUtils(context, ioDispatcher)
    }
}