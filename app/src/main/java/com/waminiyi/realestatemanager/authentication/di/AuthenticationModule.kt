package com.waminiyi.realestatemanager.authentication.di

import com.google.firebase.auth.FirebaseAuth
import com.waminiyi.realestatemanager.authentication.repository.AuthenticationRepository
import com.waminiyi.realestatemanager.authentication.repository.FirebaseAuthenticationRepository
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
    fun provideAuthenticationRepository(firebaseAuth: FirebaseAuth): AuthenticationRepository {
        return FirebaseAuthenticationRepository(firebaseAuth)
    }
}