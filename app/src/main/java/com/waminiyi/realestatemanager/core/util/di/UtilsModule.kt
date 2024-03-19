package com.waminiyi.realestatemanager.core.util.di

import android.content.Context
import com.waminiyi.realestatemanager.core.util.remdispatchers.Dispatcher
import com.waminiyi.realestatemanager.core.util.remdispatchers.RemDispatchers
import com.waminiyi.realestatemanager.core.util.util.CitiesUtils
import com.waminiyi.realestatemanager.core.util.util.NetworkUtils
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