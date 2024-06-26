package com.example.cinevote.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.Actors
import com.example.cinevote.data.database.Firestore
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.screens.signUp.firebaseAuth
import com.example.cinevote.util.TMDBService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class DetailState(
    val title:String="",
    val genres: String="" /*TODO*/,
    val plot: String="",
    val vote: Float ,
    val year: Int=0,
    val poster: String="",
    val isReviewd : Boolean = false,
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
}

class DetailsVM(private val repository: FilmRepository) : ViewModel(){
    private val _state = MutableStateFlow(DetailState(vote = 0.00F))
    val state = _state.asStateFlow()
    private val tmdbBaseUrl = "https://image.tmdb.org/t/p/w500"
    private val tmdb = TMDBService()




    val action = object : DetailAction {
        override fun showDetails(title: String) {
            viewModelScope.launch {

                try {
                    val filmDetails = repository.getFilmFromTitle(title)



                val year = filmDetails.releaseDate.substring(0, 4)




                _state.value = _state.value.copy(
                    title = filmDetails.title,
                    genres = filmDetails.genreIDs, // TODO: Converti IDs in nomi dei generi se necessario
                    plot = filmDetails.plot,
                    vote = String.format("%.1f", filmDetails.voteAverage).replace(",", ".").toFloat(),
                    poster = "$tmdbBaseUrl${filmDetails.posterPath}",
                    year = year.toInt()
                )
                }catch (_:Exception){
                    loadFromDb(title)
                }
            }
        }

        override fun loadFromDb(title: String) {

            Log.d("recupero", title)
            tmdb.fetchFilmData(
                url ="https://api.themoviedb.org/3/search/movie?query=$title&region=it&language=it",
                onSuccess = {filmList->

                    Log.d("successo", filmList.size.toString())

                    filmList.forEach { filmData ->

                        val film = FilmList(
                            filmID = filmData.id,
                            title = filmData.title,
                            posterPath =filmData.posterPath,
                            plot = filmData.plot,
                            voteAverage =filmData.voteAverage,
                            releaseDate =filmData.releaseDate,
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
                            onSuccess = {actorList->

                                Log.d("OnSuccess", actorList.size.toString())

                                actorList.forEach {actor->

                                    Log.d("actor", actor.name)

                                    _state.value = _state.value.copy(
                                        actorListState = actorList
                                    )





                                }


                            },
                            onFailure = {e->
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
                    val query = db.collection("users").whereEqualTo("mail", mail).get().await()
                    if (query.isEmpty) {
                        _state.value = state.value.copy(isReviewd = false)
                        return@launch
                    }

                    val username = query.documents[0].getString("username")
                    val query2 = db.collection("review").whereEqualTo("autore", username).whereEqualTo("titolo", title).get().await()

                    if (query2.isEmpty) {
                        _state.value = state.value.copy(isReviewd = true)
                    } else {
                        _state.value = state.value.copy(isReviewd = false)
                        val doc = query2.documents[0]
                        val stelle = doc.getLong("stelle")?.toInt()?:0
                        _state.value = state.value.copy(userVote = stelle)
                    }
                } catch (e: Exception) {
                    Log.e("hasReview", "Error while checking review", e)
                    _state.value = state.value.copy(isReviewd = false) // Gestione dell'errore, impostare a false di default
                }
            }
        }

    }
}