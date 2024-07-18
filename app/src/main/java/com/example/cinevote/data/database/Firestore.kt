package com.example.cinevote.data.database

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.Review
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.screens.signUp.firebaseAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



data class UserInfo(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val username: String = ""
)


interface FirestoreAction{
    suspend fun getDataFromMail(data:String):String

    suspend fun loadReview(title: String) : List<Review>

    suspend fun getReviewByUser(username: String) : List<Review>
    suspend fun isFavorite(title: String) : Boolean
    suspend fun loadFavorites(mail:String) : List<String>



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

        override suspend fun loadReview(title: String) : List<Review> {
            val query = db.collection("review").whereEqualTo("titolo", title).get().await()
            val list : MutableList<Review> = mutableListOf()


            for (document in query.documents) {

                val descrizione = document.getString("descrizione") ?: ""
                val stelle = document.getLong("stelle")?.toInt() ?: 0
                val autore = document.getString("autore") ?: ""

                val review = Review(descrizione = descrizione, stelle = stelle, autore = autore, titolo = title)

                list.add(review)
            }
            return list
        }

        override suspend fun getReviewByUser(username: String): List<Review> {
            val query = db.collection("review").whereEqualTo("autore", username).get().await()
            val list : MutableList<Review> = mutableListOf()


            for (document in query.documents) {

                val descrizione = document.getString("descrizione") ?: ""
                val stelle = document.getLong("stelle")?.toInt() ?: 0
                val titolo = document.getString("titolo") ?: ""

                val review = Review(descrizione = descrizione, stelle = stelle, autore = username, titolo = titolo)

                list.add(review)
            }
            return list
        }

        override suspend fun isFavorite(title: String) : Boolean {


                    val query = db.collection("favorites")
                        .whereEqualTo("user", firebaseAuth.currentUser?.email)
                        .whereEqualTo("title", title).get().await()

                    if (!query.isEmpty) {
                        //_state.value = state.value.copy(isFavorite = true)
                        return true
                    } else {
                        //_state.value = state.value.copy(isFavorite = false)
                        return false
                    }
            }

        override suspend fun loadFavorites(mail: String): List<String> {
            val list : MutableList<String> = mutableListOf()
            val query = db.collection("favorites").whereEqualTo("user", mail).get().await()
            for (document in query.documents) {
                val title = document.getString("title")
                title?.let { it ->
                    list.add(it)
                }

            }

            return list
        }


    }



}