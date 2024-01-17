package com.emmutua.vehiclehire.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.emmutua.vehiclehire.data.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

typealias SignInResponse = Response<Boolean>
typealias SignOutResponse = Response<Boolean>
typealias SignUpResponse = Response<String>

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signInEmailAndPassword(email: String, password: String): SignInResponse
    fun signOut(): SignOutResponse
    suspend fun reloadFirebaseUser()
    suspend fun signUpUser(email: String, password: String): SignUpResponse
}

class AuthAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun signInEmailAndPassword(email: String, password: String): SignInResponse {
        var signInResponse: SignInResponse = Response.Idle
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                signInResponse = Response.Success(true)
            }
            .addOnFailureListener {
                signInResponse = Response.Failure(it.localizedMessage)
            }
        reloadFirebaseUser()
        return signInResponse
    }

    override fun signOut(): SignOutResponse {
        return try {
            auth.signOut()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure("E")
        }
    }


    override suspend fun reloadFirebaseUser() {
        auth.currentUser?.reload()?.await()
    }

    override suspend fun signUpUser(email: String, password: String): SignUpResponse {
        var signUpResponse: SignUpResponse = Response.Idle
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                it.user?.sendEmailVerification()
                signUpResponse = Response.Success(it.user?.uid.orEmpty())
            }
            .addOnFailureListener {
                signUpResponse = Response.Failure(it.localizedMessage)
            }
        return signUpResponse
    }
}
