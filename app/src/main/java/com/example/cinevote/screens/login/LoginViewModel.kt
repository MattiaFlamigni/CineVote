package com.example.cinevote.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.util.TMDBService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class LoginState (
    val mail: String = "",
    val password: String = ""
)

interface LoginActions {
    fun setMail(mail: String)
    fun setPassword(password: String)
    fun isKeyCorrect(mail: String, password: String, onCompleteListener: (Boolean) -> Unit)
    fun loginGoogle()
    fun loadFilm()
}

class LoginViewModel(private val repository: FilmRepository, private val context: Context) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient



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
            auth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "signInWithEmail:success")
                        onCompleteListener(true)
                    } else {
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        onCompleteListener(false)
                    }
                }
        }

        override fun loginGoogle() {
            lateinit var mGoogleSignInClient: GoogleSignInClient
            val Req_Code: Int = 123
            lateinit var firebaseAuth: FirebaseAuth


            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("366608684761")
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(context, gso)

            val activity = context as? Activity
            val signInIntent = googleSignInClient.signInIntent
            activity?.startActivityForResult(signInIntent, RC_SIGN_IN)

            signInGoogle()
        }

        var currentPage = 1
        override fun loadFilm() {
            val tmdb = TMDBService()

            viewModelScope.launch {
                for (page in 1..100) {
                    tmdb.fetchFilmData(
                        url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=it&page=$page&sort_by=popularity.desc&region=it",
                        onSuccess = { filmList ->
                            filmList.forEach { filmData ->
                                val film = FilmList(
                                    filmID = filmData.id,
                                    title = filmData.title,
                                    plot = filmData.plot,
                                    posterPath = filmData.posterPath,
                                    releaseDate = filmData.releaseDate,
                                    genreIDs = filmData.genreIDs.toString(),
                                    voteAverage = filmData.voteAverage
                                )

                                viewModelScope.launch {
                                    withContext(Dispatchers.IO) {
                                        repository.upsert(film)
                                    }
                                }
                            }
                        },
                        onFailure = {
                            Log.d("failure database", "failure database")
                        }
                    )
                    delay(1000L)
                }
            }
        }
    }

    private fun signInGoogle() {


    }

    companion object {
        const val RC_SIGN_IN = 9001
    }


}


