package com.waminiyi.realestatemanager.authentication.di

import com.waminiyi.realestatemanager.authentication.repository.AuthRepository
import com.waminiyi.realestatemanager.authentication.repository.DefaultAuthRepository
import com.waminiyi.realestatemanager.firebase.authentication.FirebaseAuthProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuthProvider: FirebaseAuthProvider): AuthRepository {
        return DefaultAuthRepository(firebaseAuthProvider)
    }
}