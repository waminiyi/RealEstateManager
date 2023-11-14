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
     * Provides a singleton instance of [ImageDao] .
     */
    @Singleton
    @Provides
    fun provideImageDao(database: RemDatabase): ImageDao {
        return database.imageDao()
    }

    /**
     * Provides a singleton instance of [FacilityDao].
     */
    @Singleton
    @Provides
    fun provideFacilityDao(database: RemDatabase): FacilityDao {
        return database.facilityDao()
    }

    /**
     * Provides a singleton instance of [PointOfInterestDao] .
     */
    @Singleton
    @Provides
    fun providePointOfInterestDao(database: RemDatabase): PointOfInterestDao {
        return database.pointOfInterestDao()
    }

    /**
     * Provides a singleton instance of [EstateAndFacilityLinkDao] .
     */
    @Singleton
    @Provides
    fun provideEstateAndFacilityLinkDao(database: RemDatabase): EstateAndFacilityLinkDao {
        return database.estateAndFacilityLinkDao()
    }

    /**
     * Provides a singleton instance of [EstateAndPoiLinkDao] .
     */
    @Singleton
    @Provides
    fun provideEstateAndPoiLinkDao(database: RemDatabase): EstateAndPoiLinkDao {
        return database.estateAndPoiLinkDao()
    }
}
