package com.waminiyi.realestatemanager.analytics

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    abstract fun bindsAnalyticsHelper(analyticsHelperImpl: StubAnalyticsHelper): AnalyticsHelper

    companion object {
        @Provides
        @Singleton
        fun provideStubAnalyticsHelper(): StubAnalyticsHelper {
            return StubAnalyticsHelper()
        }
    }
}