package com.example.cinevote.screens.login



import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.Film
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.util.TMDBService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class LoginState (
    val mail:String="",
    val password:String=""
)

interface LoginActions{
    fun setMail(mail:String)
    fun setPassword(password:String)
    fun isKeyCorrect(mail: String, password: String, onCompleteListener: (Boolean) -> Unit)
    fun loginGoogle()


    fun loadFilm()
}
class LoginViewModel(private val repository: FilmRepository): ViewModel() {
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
                    // Introduce a delay to prevent hitting API rate limits
                    delay(1000L)
                }
            }
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



