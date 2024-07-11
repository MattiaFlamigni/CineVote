package com.example.cinevote.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.Actors
import com.example.cinevote.data.Review
import com.example.cinevote.data.database.Firestore
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.screens.signUp.firebaseAuth
import com.example.cinevote.util.TMDBService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

data class DetailState(
    val reviewList : List<Review> = emptyList(),
    val title:String="",
    val genres: List<String> = emptyList() /*TODO*/,
    val plot: String="",
    val vote: Float,
    val year: Int=0,
    val poster: String="",
    val isReviewd : Boolean = false,
    val isFavorite : Boolean = false,
    val userVote : Int=-1,
    val actorListState: List<Actors> = emptyList()
)

interface DetailAction{
    fun showDetails(title:String)
    fun loadFromDb(title:String)
    fun loadActor(id: Int)

    fun getIdFromTitle(title: String, callback: (Int) -> Unit)

    fun getKeyTrailer(id: Int, callback: (String) -> Unit)

    fun hasReview(title:String)

    fun addToWishList(title:String)

    fun isFavorite(title:String)

    fun loadReview(title:String)
    fun loadGenres(title: String): List<String>
}

class DetailsVM(private val repository: FilmRepository) : ViewModel() {

    val db = FirebaseFirestore.getInstance()
    private val _state = MutableStateFlow(DetailState(vote = 0.00F))
    val state = _state.asStateFlow()
    private val tmdbBaseUrl = "https://image.tmdb.org/t/p/w500"
    private val tmdb = TMDBService()
    val firestore = Firestore()


    val action = object : DetailAction {
        override fun showDetails(title: String) {
            viewModelScope.launch {

                try {
                    val filmDetails = repository.getFilmFromTitle(title)


                    val year = filmDetails.releaseDate.substring(0, 4)




                    _state.value = _state.value.copy(
                        title = filmDetails.title,
                        //genres = filmDetails.genreIDs, // TODO: Converti IDs in nomi dei generi se necessario
                        plot = filmDetails.plot,
                        vote = String.format("%.1f", filmDetails.voteAverage).replace(",", ".")
                            .toFloat(),
                        poster = "$tmdbBaseUrl${filmDetails.posterPath}",
                        year = year.toInt()
                    )
                } catch (_: Exception) {
                    loadFromDb(title)
                }
            }
        }

        override fun loadFromDb(title: String) {

            Log.d("recupero", title)
            tmdb.fetchFilmData(
                url = "https://api.themoviedb.org/3/search/movie?query=$title&region=it&language=it",
                onSuccess = { filmList ->

                    Log.d("successo", filmList.size.toString())

                    filmList.forEach { filmData ->

                        val film = FilmList(
                            filmID = filmData.id,
                            title = filmData.title,
                            posterPath = filmData.posterPath,
                            plot = filmData.plot,
                            voteAverage = filmData.voteAverage,
                            releaseDate = filmData.releaseDate,
                            genreIDs = filmData.genreIDs.toString()

                        )

                        Log.d("filmlist", film.toString())

                        viewModelScope.launch {
                            withContext(Dispatchers.IO) {
                                repository.upsert(film)
                            }
                        }
                    }

                },
                onFailure = {}
            )
        }

        override fun loadActor(id: Int) {
            viewModelScope.launch {
                try {
                    val actorList = withContext(Dispatchers.IO) {
                        tmdb.fetchActorFromFilmID(
                            url = "https://api.themoviedb.org/3/movie/$id/credits",
                            onSuccess = { actorList ->

                                Log.d("OnSuccess", actorList.size.toString())

                                actorList.forEach { actor ->

                                    Log.d("actor", actor.name)

                                    _state.value = _state.value.copy(
                                        actorListState = actorList
                                    )


                                }


                            },
                            onFailure = { e ->
                                Log.e("loadActor", "Errore nella richiesta HTTP", e)

                            }
                        )
                    }


                } catch (e: Exception) {
                    // Gestisci eventuali eccezioni qui
                    Log.e("loadActor", "Errore durante il caricamento dell'attore", e)
                }
            }
        }


        //bug precedente: La richiesta era asincrona, veniva restituito il valore di default prima che la richiesta fosse stata completata
        override fun getIdFromTitle(title: String, callback: (Int) -> Unit) {
            viewModelScope.launch {
                val id = withContext(Dispatchers.IO) {
                    repository.getTitleById(title)
                }
                callback(id)
            }
        }

        override fun getKeyTrailer(id: Int, callback: (String) -> Unit) {
            tmdb.fetchTrailerFilm(id, onSuccess = { trailerKey ->
                callback(trailerKey)
            }, onFailure = {
                // Gestire il caso in cui il recupero del trailer fallisce
            })
        }

        override fun hasReview(title: String) {
            val db = FirebaseFirestore.getInstance()

            viewModelScope.launch {
                val mail = firebaseAuth.currentUser?.email
                try {
                    var username = ""
                    val query = db.collection("users").whereEqualTo("mail", mail).get().await()
                    if (query.isEmpty) {
                        /*_state.value = state.value.copy(isReviewd = false)
                        return@launch*/
                        username = firebaseAuth.currentUser?.displayName.toString()
                    } else {

                        username = query.documents[0].getString("username").toString()
                    }
                    val query2 = db.collection("review").whereEqualTo("autore", username)
                        .whereEqualTo("titolo", title).get().await()

                    if (query2.isEmpty) {
                        _state.value = state.value.copy(isReviewd = true)
                    } else {
                        _state.value = state.value.copy(isReviewd = false)
                        val doc = query2.documents[0]
                        val stelle = doc.getLong("stelle")?.toInt() ?: 0
                        _state.value = state.value.copy(userVote = stelle)
                    }

                } catch (e: Exception) {
                    Log.e("hasReview", "Error while checking review", e)
                    _state.value =
                        state.value.copy(isReviewd = false) // Gestione dell'errore, impostare a false di default
                }
            }
        }

        override fun addToWishList(title: String) {


            val currentUser = firebaseAuth.currentUser?.email
            val document = hashMapOf("title" to title, "user" to currentUser)

            if (!state.value.isFavorite) {
                db.collection("favorites").add(document)
                _state.value = state.value.copy(isFavorite = true)
            } else {
                viewModelScope.launch {
                    var query = db.collection("favorites").whereEqualTo("user", currentUser)
                        .whereEqualTo("title", title).get().await()

                    for (document in query.documents) {
                        db.collection("favorites").document(document.id).delete()
                    }
                    _state.value = state.value.copy(isFavorite = false)
                }

            }

        }

        override fun isFavorite(title: String) {
            viewModelScope.launch {
                try {
                    val query = db.collection("favorites")
                        .whereEqualTo("user", firebaseAuth.currentUser?.email)
                        .whereEqualTo("title", title)
                        .get()
                        .await()

                    if (!query.isEmpty) {
                        _state.value = state.value.copy(isFavorite = true)
                    } else {
                        _state.value = state.value.copy(isFavorite = false)
                    }
                } catch (e: Exception) {
                    //_state.value = state.value.copy(isFavorite = false)
                    Log.e("FAVORITE", "Error while checking favorite", e)
                }
            }
        }

        override fun loadReview(title: String) {
            /*val list : MutableList<Review> = mutableListOf()
            viewModelScope.launch {
                val query = db.collection("review").whereEqualTo("titolo", title).get().await()

                    for (document in query.documents) {

                        val descrizione = document.getString("descrizione") ?: ""
                        val stelle = document.getLong("stelle")?.toInt() ?: 0
                        val autore = document.getString("autore") ?: ""

                        val review = Review(descrizione = descrizione, stelle = stelle, autore = autore, titolo = title)
                        list.add(review)
                    }
                _state.value = state.value.copy(reviewList = list)

                Log.d("caricamento recensioni", state.value.reviewList.toString())

            }*/


            viewModelScope.launch {
                val review = firestore.actions.loadReview(title)
                _state.value = state.value.copy(reviewList = review)

            }


        }

        override fun loadGenres(title: String): List<String> {
            val genresNames = mutableListOf<String>()

            viewModelScope.launch {
                try {
                    val film = repository.getFilmFromTitle(title = title)
                    Log.d("generi", "film : $film")
                    val genresIDsString = film.genreIDs
                    Log.d("generi", "generi : $genresIDsString")




                    val numeriStringa = genresIDsString
                        .replace("[", "")
                        .replace("]", "")
                        .trim()

                    // Dividere la stringa in array di stringhe separate dai numeri
                    val numeriArray = numeriStringa.split(", ")

                    // Convertire le stringhe in numeri interi
                    val genreIdList = numeriArray.map { it.toInt() }
                    Log.d("generi", genreIdList.toString())

                    val client = OkHttpClient()

                    val request = Request.Builder()
                        .url("https://api.themoviedb.org/3/genre/movie/list?language=it")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader(
                            "Authorization",
                            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMTNhOGQwMGFhYjU1MDIwN2FlMDBiMDliZDBlNDIxMyIsIm5iZiI6MTcxOTY0NzUxNS40Nzc0NTUsInN1YiI6IjY1ZjcxZWZkMjQyZjk0MDE3ZGNjZjQxNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.MTTGz_kRPsw9kkdiEm1lDFtyPX7zna33BQuTNoF3rHc"
                        )
                        .build()

                    val response: Response = withContext(Dispatchers.IO) {
                        client.newCall(request).execute()
                    }

                    if (response.isSuccessful) {
                        val responseBody = response.body().string()
                        responseBody?.let {
                            val jsonObject = JSONObject(it)
                            val genresArray = jsonObject.getJSONArray("genres")
                            Log.d("generi", "size : ${genresArray.length()}")
                            for (i in 0 until genresArray.length()) {
                                val genreObject = genresArray.getJSONObject(i)
                                val genreName = genreObject.getString("name")
                                val genreID = genreObject.getInt("id")
                                Log.d("generi", "nome : $genreID")
                                if(genreIdList.contains(genreID)){
                                    genresNames.add(genreName)
                                }

                            }


                            Log.d("generi", "lista finale : $genresNames")
                            _state.value = state.value.copy(genres = genresNames)
                        }
                    } else {
                        // Handle unsuccessful response
                        // For example, log the error or throw an exception
                        Log.d("generi", "fallito")
                    }


                } catch (e: Exception) {
                    // Handle exceptions
                }
            }





            return genresNames
        }

        // Funzione per il parsing della risposta JSON e ottenere i nomi dei generi corrispondenti agli ID forniti
        private fun parseGenreIDs(genreIDsString: String): List<Int> {
            val trimmed = genreIDsString.trim('[', ']').replace("\\s".toRegex(), "")
            if (trimmed.isEmpty()) {
                return emptyList()
            }
            return trimmed.split(",").map { it.trim().toInt() }
        }
    }
}