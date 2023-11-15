package com.waminiyi.realestatemanager.authentication.repository

import com.waminiyi.realestatemanager.authentication.model.User
import com.waminiyi.realestatemanager.authentication.model.Result

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): Result<Boolean>
    suspend fun signUp(email: String, password: String): Result<Boolean>
    suspend fun signOut(): Result<Boolean>
    fun getCurrentUser(): User?
}