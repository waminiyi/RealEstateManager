package com.waminiyi.realestatemanager.core.data.remote.di

import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirestoreDao(firestore: FirebaseFirestore): FirestoreDao {
        return FirestoreDao(firestore)
    }
}