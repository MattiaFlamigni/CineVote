package com.example.cinevote.screens.settings

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.database.Firestore
import com.example.cinevote.screens.signUp.firebaseAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class SettingsStatus(
    val name:String="",
    val surname:String="",
    val username:String="",
    val path:Uri= Uri.EMPTY
)

interface SettingsAction {
    fun logOut()

    fun updateProfileImage(uri:Uri)
    fun getProfilePic(mail:String)
}

class SettingsVm : ViewModel() {

    val firestore = Firestore()
    val auth = FirebaseAuth.getInstance()

    private val _state= MutableStateFlow(SettingsStatus())
    val state = _state.asStateFlow()

    init{
        fetchUserData()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            val username = firestore.actions.getDataFromMail("username")
            val name = auth.currentUser?.displayName?:"Utente non registrato"
            _state.update { it.copy(username=username, name = name) }
        }
    }

    val action = object : SettingsAction {
        override fun logOut() {
            auth.signOut()

        }

        override fun updateProfileImage(uri:Uri) {

            val db = FirebaseFirestore.getInstance()
            val profileData = hashMapOf(
                "user" to (firebaseAuth.currentUser?.email ?: ""), // Sostituisci con l'ID dell'utente
                "profilePicUri" to uri.toString() // Converte l'URI in una stringa per salvarla in Firestore
                // Puoi aggiungere altri campi qui se necessario
            )

            db.collection("profilePIC")
                .add(profileData)
                .addOnSuccessListener { documentReference ->
                    // Operazione completata con successo
                    println("DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    // Gestione degli errori
                    println("Error adding document: $e")
                }



        }

        override fun getProfilePic(mail: String) {
            val db = FirebaseFirestore.getInstance()

            viewModelScope.launch {
                val query = db.collection("profilePIC").whereEqualTo("user", mail).get().await()
                for(document in query.documents){
                    val path = document.getString("profilePicUri")?:""
                    _state.value = state.value.copy(path = Uri.parse(path))
                    Log.d("pathURI", state.value.path.toString())
                }
            }
        }

    }







}

