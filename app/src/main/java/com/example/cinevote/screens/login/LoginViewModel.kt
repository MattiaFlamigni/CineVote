package com.example.cinevote.screens.login


import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cinevote.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity.getSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update





import android.content.Intent
import android.credentials.GetCredentialRequest
import android.credentials.GetCredentialResponse
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.credentials.CustomCredential
import androidx.credentials.PasswordCredential
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
//import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseUser

data class LoginState (
    val mail:String="",
    val password:String=""
)

interface LoginActions{
    fun setMail(mail:String)
    fun setPassword(password:String)
    fun isKeyCorrect(mail: String, password: String, onCompleteListener: (Boolean) -> Unit)
    fun loginGoogle()
}
class LoginViewModel: ViewModel() {
    private val _state= MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    val actions = object : LoginActions {
        override fun setMail(mail: String) {
            _state.update { it.copy(mail = mail) }
        }

        override fun setPassword(password: String) {
            _state.update { it.copy(password = password) }
        }

        override fun isKeyCorrect(
            mail: String,
            password: String,
            onCompleteListener: (Boolean) -> Unit
        ) {
            val auth: FirebaseAuth = Firebase.auth

            auth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success")
                        val currentUser: FirebaseUser? = auth.currentUser
                        onCompleteListener(true)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        onCompleteListener(false)
                    }
                }
        }


        override fun loginGoogle() {
        }
    }
}




private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
    try {
        val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
        // L'accesso è stato eseguito correttamente, puoi utilizzare 'account' per ottenere informazioni sull'utente

        // Esempio di utilizzo dei dati dell'account:
        val idToken = account?.idToken // Ottieni l'idToken per l'autenticazione del backend
        val email = account?.email // Ottieni l'email dell'utente

        // Ora puoi utilizzare l'idToken per l'autenticazione lato server (se necessario)

    } catch (e: ApiException) {
        // L'accesso non è riuscito, gestisci l'errore appropriatamente
        Log.w("TAG", "signInResult:failed code=" + e.statusCode)
    }
}



