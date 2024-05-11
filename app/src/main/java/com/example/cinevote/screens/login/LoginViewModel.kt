package com.example.cinevote.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginState (
    val mail:String="",
    val password:String=""
)

interface LoginActions{
    fun setMail(mail:String)
    fun setPassword(password:String)
    fun isKeyCorrect(mail: String, password: String, onCompleteListener: (Boolean) -> Unit)

}
class LoginViewModel: ViewModel() {
    private val _state= MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    val actions = object : LoginActions {
        override fun setMail(mail: String) {
            _state.update { it.copy(mail=mail) }
        }

        override fun setPassword(password: String) {
            _state.update { it.copy(password=password) }
        }

        override fun isKeyCorrect(mail: String, password: String, onCompleteListener: (Boolean) -> Unit) {
            val auth: FirebaseAuth = Firebase.auth

            auth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success")
                        onCompleteListener(true)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        onCompleteListener(false)
                    }
                }
        }


    }

}