package com.waminiyi.realestatemanager.authentication.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.waminiyi.realestatemanager.authentication.model.Result
import com.waminiyi.realestatemanager.authentication.model.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthenticationRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthenticationRepository {
    override suspend fun signIn(email: String, password: String): Result<Boolean> {
        val signInTask = firebaseAuth.signInWithEmailAndPassword(email, password)
        return handleAuthenticationTask(signInTask)
    }

    override suspend fun signUp(email: String, password: String): Result<Boolean> {
        val signUpTask = firebaseAuth.createUserWithEmailAndPassword(email, password)
        return handleAuthenticationTask(signUpTask)
    }

    private suspend fun handleAuthenticationTask(task: Task<AuthResult>): Result<Boolean> {
        return suspendCoroutine { continuation ->
            task.addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    continuation.resume(Result.Success(true))
                } else {
                    val exception = authTask.exception
                    val errorMessage = exception?.message ?: "Authentication failed"
                    continuation.resume(Result.Error(Exception(errorMessage)))
                }

            }
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        firebaseAuth.signOut()
        return Result.Success(true)
    }

    override fun getCurrentUser(): User? {
        val currentUser = firebaseAuth.currentUser
        return currentUser?.let {
            User(it.uid, it.email ?: "")
        }
    }
}