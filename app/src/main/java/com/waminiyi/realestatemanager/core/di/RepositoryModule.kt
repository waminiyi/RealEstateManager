package com.waminiyi.realestatemanager.core.di

import android.content.Context
import com.waminiyi.realestatemanager.core.data.repository.AgentRepository
import com.waminiyi.realestatemanager.core.data.repository.DefaultAgentRepository
import com.waminiyi.realestatemanager.core.data.repository.DefaultEstateRepository
import com.waminiyi.realestatemanager.core.data.repository.DefaultMediaFileRepository
import com.waminiyi.realestatemanager.core.data.repository.DefaultPhotoRepository
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.data.repository.FilterRepository
import com.waminiyi.realestatemanager.core.data.repository.MediaFileRepository
import com.waminiyi.realestatemanager.core.data.repository.PhotoRepository
import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
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