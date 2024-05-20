package com.example.cinevote.data.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await



data class UserInfo(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val username: String = ""
)


interface FirestoreAction{
    suspend fun getDataFromMail(data:String):String
}

class Firestore {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")
    private val _state= MutableStateFlow(UserInfo())
    val state = _state.asStateFlow()

    val actions = object : FirestoreAction {
        override suspend fun getDataFromMail(data:String):String {

            val userEmail = auth.currentUser?.email ?: ""

            Log.d("usermail", userEmail)

            if (userEmail.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()
                val usersCollection = db.collection("users")

                // Effettua una query per ottenere il documento dell'utente corrispondente all'email
                val querySnapshot = usersCollection.whereEqualTo("mail", userEmail).get().await()

                // Verifica se è stato trovato un documento corrispondente all'utente
                if (!querySnapshot.isEmpty) {
                    // Ottieni il primo documento (ci dovrebbe essere solo uno)
                    val userDocument = querySnapshot.documents.first()

                    // Ottieni il valore del campo "username" dal documento
                    val value = userDocument.getString(data) ?: ""

                    return value
                }
            }

            // Se non è stato trovato un documento corrispondente all'utente o se l'email è vuota, restituisci una stringa vuota
            return ""
        }

    }



}