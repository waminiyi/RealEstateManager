package com.waminiyi.realestatemanager.core.data.di

import android.content.Context
import com.waminiyi.realestatemanager.core.data.remote.repository.FirestoreRepository
import com.waminiyi.realestatemanager.core.data.remote.repository.RemoteDataRepository
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
import com.waminiyi.realestatemanager.core.database.dao.LocalChangeDao
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.firebase.firestore.FirestoreDao
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
        agentDao: AgentDao,
        localChangeDao: LocalChangeDao,
        remoteDataRepository: RemoteDataRepository
    ): AgentRepository {
        return DefaultAgentRepository(agentDao, localChangeDao, remoteDataRepository)
    }

    @Provides
    @Singleton
    fun provideEstateRepository(
        estateDao: EstateDao,
        localChangeDao: LocalChangeDao,
        remoteDataRepository: RemoteDataRepository,
        filterRepository: FilterRepository
    ): EstateRepository {
        return DefaultEstateRepository(estateDao, localChangeDao, remoteDataRepository, filterRepository)
    }

    @Provides
    @Singleton
    fun providePhotoRepository(
        photoDao: PhotoDao,
        mediaFileRepository: MediaFileRepository,
        localChangeDao: LocalChangeDao,
        remoteDataRepository: RemoteDataRepository
    ): PhotoRepository {
        return DefaultPhotoRepository(photoDao, mediaFileRepository, localChangeDao, remoteDataRepository)
    }

    @Provides
    @Singleton
    fun provideRemoteDataRepository(
        firestoreDao: FirestoreDao
    ): RemoteDataRepository {
        return FirestoreRepository(firestoreDao)
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