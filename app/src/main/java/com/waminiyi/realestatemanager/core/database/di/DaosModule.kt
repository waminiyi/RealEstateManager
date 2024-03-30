package com.waminiyi.realestatemanager.core.database.di

import com.waminiyi.realestatemanager.core.database.RemDatabase
import com.waminiyi.realestatemanager.core.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module providing instances of Data Access Objects (DAOs) and the [RemDatabase] class.
 * This module ensures that single instances of DAOs are available throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    /**
     * Provides a singleton instance of [EstateDao] .
     */
    @Singleton
    @Provides
    fun provideEstateDao(database: RemDatabase): EstateDao {
        return database.estateDao()
    }

    /**
     * Provides a singleton instance of [AgentDao] .
     */
    @Singleton
    @Provides
    fun provideAgentDao(database: RemDatabase): AgentDao {
        return database.agentDao()
    }

    /**
     * Provides a singleton instance of [PhotoDao] .
     */
    @Singleton
    @Provides
    fun provideImageDao(database: RemDatabase): PhotoDao {
        return database.photoDao()
    }

}
