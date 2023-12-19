package com.waminiyi.realestatemanager.firebase.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.waminiyi.realestatemanager.firebase.model.FirebaseResult
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthProvider @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signIn(email: String, password: String): FirebaseResult<FirebaseUser?> {
        val signInTask = firebaseAuth.signInWithEmailAndPassword(email, password)
        return handleAuthenticationTask(signInTask)
    }

    suspend fun signUp(email: String, password: String): FirebaseResult<FirebaseUser?> {
        val signUpTask = firebaseAuth.createUserWithEmailAndPassword(email, password)
        return handleAuthenticationTask(signUpTask)
    }

    private suspend fun handleAuthenticationTask(task: Task<AuthResult>): FirebaseResult<FirebaseUser?> {
        return suspendCoroutine { continuation ->
            task.addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    continuation.resume(FirebaseResult.Success(authTask.result.user))
                } else {
                    val exception = authTask.exception
                    val errorMessage = exception?.message ?: "Authentication failed"
                    continuation.resume(FirebaseResult.Error(Exception(errorMessage)))
                }
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}