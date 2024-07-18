package com.example.cinevote.screens.outNow

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.Film

import com.example.cinevote.util.TMDBService
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import org.threeten.bp.LocalDate

data class OutNowStatus(val filmList: List<Film> = emptyList())

interface FilmAction {
    fun getFilmList()
    fun getFutureFilm()
}


class OutNowVM : ViewModel() {

    val selectedFilter: MutableState<TypeFilm> = mutableStateOf(TypeFilm.AL_CINEMA)
    private val tmdb = TMDBService()

    private val _state = MutableStateFlow(OutNowStatus())
    val state = _state.asStateFlow()

    val action = object : FilmAction {
        override fun getFilmList() {

            viewModelScope.launch(Dispatchers.IO) {

                val url =
                    "https://api.themoviedb.org/3/movie/now_playing?language=it&page=1&region=it"

                //"https://api.themoviedb.org/3/discover/movie?include_adult=true&include_video=false&language=it&page=1&primary_release_date.gte=2024-06-17&region=it&sort_by=popularity.desc&with_release_type=3"
                tmdb.fetchFilmData(
                    url,
                    onSuccess = { filmList ->
                        _state.update { it.copy(filmList = filmList) }
                    },
                    onFailure = {
                        Log.e("OutNowVM", "Errore nella richiesta")
                    }
                )
            }


        }

        override fun getFutureFilm() {
            viewModelScope.launch(Dispatchers.IO) {

                val currentDate = LocalDate.now()
                val url =
                    //"https://api.themoviedb.org/3/movie/now_playing?language=it&page=1&region=it"

                "https://api.themoviedb.org/3/discover/movie?include_adult=true&include_video=false&language=it&page=1&primary_release_date.gte=${currentDate}&region=it&sort_by=popularity.desc&with_release_type=3"
                tmdb.fetchFilmData(
                    url,
                    onSuccess = { filmList ->
                        _state.update { it.copy(filmList = filmList) }
                    },
                    onFailure = {
                        Log.e("OutNowVM", "Errore nella richiesta")
                    }
                )
            }
        }
    }
}


private fun parseFilmData(jsonData: String): List<Film> {
    val filmList = mutableListOf<Film>()
    try {
        val jsonObject = JSONObject(jsonData)
        val resultsArray = jsonObject.getJSONArray("results")
        for (i in 0 until resultsArray.length()) {
            val filmObject = resultsArray.getJSONObject(i)
            val id = filmObject.getInt("id")
            val title = filmObject.getString("title")
            val posterPath = filmObject.getString("poster_path")
            val plot = filmObject.getString("overview")
            val voteAverage = filmObject.getInt("vote_average").toFloat()
            val releaseDate = filmObject.getString("release_date")
            val genreIDsArray = filmObject.getJSONArray("genre_ids")
            val genreIDs = mutableListOf<Int>()
            for (i in 0 until genreIDsArray.length()) {
                val genreID = genreIDsArray.getInt(i)
                genreIDs.add(genreID)
            }


            val film = Film(id, title, posterPath, plot, voteAverage, releaseDate, genreIDs)
            filmList.add(film)
            /*if (releaseDateLocalDate.isAfter(dueMesiFa) || releaseDateLocalDate.isEqual(dueMesiFa)) {

            }*/
        }
    } catch (e: JSONException) {
        // Gestisci l'eccezione se ci sono problemi nell'analisi dei dati JSON
        Log.e("OutNowVM", "Errore nell'analisi dei dati JSON: ${e.message}")
    }
    return filmList
}



