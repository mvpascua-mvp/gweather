package com.example.gweather.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun register(email: String, password: String): Result<FirebaseUser?> =
        suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) continuation.resume(Result.success(firebaseAuth.currentUser))
                    else continuation.resume(
                        Result.failure(
                            task.exception ?: Exception("Unknown error")
                        )
                    )
                }
        }

    suspend fun login(email: String, password: String): Result<FirebaseUser?> =
        suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) continuation.resume(Result.success(firebaseAuth.currentUser))
                    else continuation.resume(Result.failure(
                        task.exception ?: Exception("Unknown error")
                        )
                    )
                }
        }

    fun currentUser(): FirebaseUser? = firebaseAuth.currentUser
    fun logout() = firebaseAuth.signOut()
}