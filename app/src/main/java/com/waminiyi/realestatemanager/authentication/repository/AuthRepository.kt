package com.waminiyi.realestatemanager.authentication.repository

import com.waminiyi.realestatemanager.authentication.model.Result
import com.waminiyi.realestatemanager.authentication.model.User

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
    fun signOut()
    fun getCurrentUser(): User?
}