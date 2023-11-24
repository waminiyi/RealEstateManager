package com.waminiyi.realestatemanager.core.data.di

import com.waminiyi.realestatemanager.core.data.repository.AgentRepository
import com.waminiyi.realestatemanager.core.data.repository.DefaultAgentRepository
import com.waminiyi.realestatemanager.core.data.repository.DefaultEstateRepository
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAgentRepository(
        agentDao: AgentDao,
    ): AgentRepository {
        return DefaultAgentRepository(agentDao)
    }

    @Provides
    @Singleton
    fun provideEstateRepository(
        estateDao: EstateDao,
        photoDao: PhotoDao
    ): EstateRepository {
        return DefaultEstateRepository(estateDao, photoDao)
    }
}