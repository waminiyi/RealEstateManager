package com.waminiyi.realestatemanager.core.sync.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.samples.apps.nowinandroid.sync.status.SyncSubscriber
import com.waminiyi.realestatemanager.core.messenger.RemFirebaseMessenger
import com.waminiyi.realestatemanager.core.messenger.RemMessenger
import com.waminiyi.realestatemanager.core.sync.status.FirebaseSyncSubscriber
import com.waminiyi.realestatemanager.core.sync.status.WorkManagerSyncManager
import com.waminiyi.realestatemanager.core.util.sync.SyncManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SyncModule {
    @Binds
    fun bindsSyncStatusMonitor(
        syncStatusMonitor: WorkManagerSyncManager,
    ): SyncManager

    @Binds
    fun bindsSyncSubscriber(
        syncSubscriber: FirebaseSyncSubscriber,
    ): SyncSubscriber

    @Binds
    fun bindsRemMessenger(
        syncSubscriber: RemFirebaseMessenger,
    ): RemMessenger

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseMessaging(): FirebaseMessaging = Firebase.messaging
    }
}