package com.waminiyi.realestatemanager.firebase.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.waminiyi.realestatemanager.firebase.firestore.CloudStorage
import com.waminiyi.realestatemanager.firebase.firestore.FirestoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    @Singleton
    fun provideFirestoreStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideCloudStorage(firestoreStorage: FirebaseStorage): CloudStorage {
        return CloudStorage(firestoreStorage)
    }
}