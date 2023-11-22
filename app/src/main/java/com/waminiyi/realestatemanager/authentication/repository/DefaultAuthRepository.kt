package com.waminiyi.realestatemanager.authentication.repository

import com.waminiyi.realestatemanager.authentication.model.Result
import com.waminiyi.realestatemanager.authentication.model.User
import com.waminiyi.realestatemanager.firebase.authentication.FirebaseAuthProvider
import com.waminiyi.realestatemanager.firebase.model.FirebaseResult.Error
import com.waminiyi.realestatemanager.firebase.model.FirebaseResult.Success
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val firebaseAuthProvider: FirebaseAuthProvider
) : AuthRepository {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return when (val result = firebaseAuthProvider.signIn(email, password)) {
            is Success -> {
                if (result.data != null) {
                    Result.Success(Unit)
                } else {
                    Result.Error(Exception("User not found"))
                }
            }
            is Error -> {
                Result.Error(result.exception)
            }
        }
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        return when (val result = firebaseAuthProvider.signUp(email, password)) {
            is Success -> {
                if (result.data != null) {
                    Result.Success(Unit)
                } else {
                    Result.Error(Exception("User not found"))
                }
            }
            is Error -> {
                Result.Error(result.exception)
            }
        }
    }

    override fun signOut() {
        firebaseAuthProvider.signOut()
    }

    override fun getCurrentUser(): User? {
        return firebaseAuthProvider.getCurrentUser()?.let {
            User(it.uid, it.email ?: "")
        }
    }
}