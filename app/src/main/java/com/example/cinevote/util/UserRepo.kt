package com.example.cinevote.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


data class UserInfo(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val username: String = ""
)


class UserRepo {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun getUserInfo(): UserInfo? {
        val currentUserEmail = auth.currentUser?.email ?: ""
        if (currentUserEmail.isNotEmpty()) {
            val querySnapshot = usersCollection.whereEqualTo("email", currentUserEmail).get().await()
            if (!querySnapshot.isEmpty) {
                val userDocument = querySnapshot.documents.first()
                return userDocument.toObject(UserInfo::class.java)
            }
        }
        return null
    }



}