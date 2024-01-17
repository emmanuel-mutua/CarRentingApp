package com.emmutua.vehiclehire.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.emmutua.vehiclehire.presentation.auth.UserData
import kotlinx.coroutines.tasks.await


interface StorageService {
    suspend fun addUser(user: UserData): Boolean
    suspend fun getUserData(uid: String, onSuccess: (DocumentSnapshot) -> Unit)
}

class StorageServiceImpl(
    private val db: FirebaseFirestore,
) : StorageService {
    override suspend fun addUser(user: UserData): Boolean {
        return try {
            db.collection("users").document(user.uid).set(user)
                .await()
            Log.d("FireStore", "User Registred success")
            true
        } catch (e: Exception) {
            Log.d("FireStore", e.localizedMessage)
            false
        }
    }

    override suspend fun getUserData(
        uid: String,
        onSuccess: (DocumentSnapshot) -> Unit,
    ) {
        try {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    onSuccess(document)
                }.await()
        } catch (e: Exception) {
            Log.d("FireStore", e.localizedMessage)
        }
    }
}
