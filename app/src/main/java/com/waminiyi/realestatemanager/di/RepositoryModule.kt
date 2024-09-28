package com.waminiyi.realestatemanager.di

import android.content.Context
import com.waminiyi.realestatemanager.data.repository.AgentRepository
import com.waminiyi.realestatemanager.data.repository.DefaultAgentRepository
import com.waminiyi.realestatemanager.data.repository.DefaultEstateRepository
import com.waminiyi.realestatemanager.data.repository.DefaultMediaFileRepository
import com.waminiyi.realestatemanager.data.repository.DefaultPhotoRepository
import com.waminiyi.realestatemanager.data.repository.EstateRepository
import com.waminiyi.realestatemanager.data.repository.FilterRepository
import com.waminiyi.realestatemanager.data.repository.MediaFileRepository
import com.waminiyi.realestatemanager.data.repository.PhotoRepository
import com.waminiyi.realestatemanager.data.database.dao.AgentDao
import com.waminiyi.realestatemanager.data.database.dao.EstateDao
import com.waminiyi.realestatemanager.data.database.dao.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAgentRepository(
        agentDao: AgentDao
    ): AgentRepository {
        return DefaultAgentRepository(agentDao)
    }

    @Provides
    @Singleton
    fun provideEstateRepository(
        estateDao: EstateDao,
        filterRepository: FilterRepository
    ): EstateRepository {
        return DefaultEstateRepository(estateDao, filterRepository)
    }

    @Provides
    @Singleton
    fun providePhotoRepository(
        photoDao: PhotoDao,
    ): PhotoRepository {
        return DefaultPhotoRepository(photoDao)
    }


    @Provides
    @Singleton
    fun provideMediaFileRepository(
        @ApplicationContext context: Context
    ): MediaFileRepository {
        return DefaultMediaFileRepository(context)
    }

    @Singleton
    @Provides
    fun provideFilterRepository(): FilterRepository {
        return FilterRepository()
    }
}