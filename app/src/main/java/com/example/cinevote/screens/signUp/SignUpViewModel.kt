package com.example.cinevote.screens.signUp


import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// ...

val firebaseAuth = FirebaseAuth.getInstance()


data class SignupState(
    val name:String="",
    val surname:String="",
    val username:String="",
    val mail :String ="",
    val password:String=""
)

data class User(
    val name: String,
    val surname:String,
    val username : String,
    val mail : String
)

interface SignUPActions{
    fun setName(name:String)
    fun setSurname(surname:String)
    fun setUsername(username:String)
    fun setMail(mail:String)
    fun setPassword(password:String)
    fun createUser(
        name:String,
        surname:String,
        username:String,
        mail:String,
        password: String
    )
}
class SignupViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()




    val action= object :SignUPActions{
        override fun setName(name: String) {
            _state.update { it.copy(name=name) }
        }

        override fun setSurname(surname: String) {
            _state.update { it.copy(surname=surname) }
        }

        override fun setUsername(username: String) {
            _state.update { it.copy(username=username) }
        }

        override fun setMail(mail: String) {
            _state.update { it.copy(mail=mail) }
        }

        override fun setPassword(password: String) {
            _state.update { it.copy(password=password) }
        }

        override fun createUser(
            name: String,
            surname: String,
            username: String,
            mail: String,
            password: String
        ) {


            // Create the user in Firebase
            firebaseAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Update the user profile
                        val user = firebaseAuth.currentUser
                        user?.updateProfile(
                            UserProfileChangeRequest.Builder()
                            .setDisplayName("$name $surname")
                            .build())
                            ?.addOnCompleteListener { profileTask ->
                                /*if (profileTask.isSuccessful) {
                                    _state.update { it.copy(success = true, loading = false) }
                                } else {
                                    _state.update { it.copy(error = "Error updating user profile", loading = false) }
                                }*/
                            }
                        writeNewUser(username, name, surname, username, mail)
                    } else {
                        //_state.update { it.copy(error = task.exception?.message ?: "Error creating user", loading = false) }
                    }
                }
        }

    }
}


private fun writeNewUser(userId: String, name: String, surname: String, username: String, mail:String) {

    /*var database = Firebase.database.reference

    val user = User(name,surname)

    database.child("users").child(userId).setValue(user)*/

    val db = Firebase.firestore
    val user = User(name,surname, username, mail)

    db.collection("users").add(user)


}

fun isUsernameAvailable(username: String, completion: (Boolean) -> Unit) {


    /*TODO: USE FIREBASE DATABASE TO CHECK IF USERNAME IS AVAILABLE*/
    /*val database = Firebase.database.reference
    database.child("users").child(username).get()
        .addOnSuccessListener {
            // Username is available if the snapshot doesn't exist
            completion(it.exists().not())
        }
        .addOnFailureListener {
            // Handle error, e.g., log the error and return false
            Log.e("Firebase", "Error checking username availability", it)
            completion(false)
        }*/



    val db = Firebase.firestore

    db.collection("users")
        .whereEqualTo("username", username)
        .get()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Se ci sono documenti restituiti dalla query, significa che l'username esiste già
                if (task.result?.isEmpty == false) {
                    completion(false)
                } else {
                    completion(true)
                }
            } else {
                // Gestisci il fallimento dell'operazione
                completion(false)
                Log.e("TAG", "Errore durante il recupero dei documenti dalla collezione 'users'", task.exception)
            }
        }
}
