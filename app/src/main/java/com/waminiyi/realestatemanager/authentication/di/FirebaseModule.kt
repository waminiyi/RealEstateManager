package com.waminiyi.realestatemanager.authentication.di

import com.google.firebase.auth.FirebaseAuth
import com.waminiyi.realestatemanager.firebase.authentication.FirebaseAuthProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthProvider(firebaseAuth: FirebaseAuth): FirebaseAuthProvider {
        return FirebaseAuthProvider(firebaseAuth)
    }
}