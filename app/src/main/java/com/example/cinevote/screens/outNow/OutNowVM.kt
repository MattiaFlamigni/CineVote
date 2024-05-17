package com.example.cinevote.screens.outNow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.cinevote.screens.cinema.Cinema
import java.time.LocalDate
import com.example.cinevote.screens.cinema.DISTANCE
import com.example.cinevote.util.TMDBService

import java.time.format.DateTimeFormatter
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import java.time.*
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.sql.Date
import java.util.Calendar

data class OutNowStatus(val filmList: List<Film> = emptyList())

interface FilmAction {
    fun getFilmList()
}

data class Film(
    val title: String,
    val posterPath: String,
    val plot: String,
    //val actors: List<String>,
    val voteAverage: Int,
    val releaseDate : String
) {
    val posterUrl: String
        get() = "https://image.tmdb.org/t/p/w500$posterPath"
}

class OutNowVM : ViewModel() {

    private val tmdb = TMDBService()

    private val _state = MutableStateFlow(OutNowStatus())
    val state = _state.asStateFlow()

    val action = object : FilmAction {
        override fun getFilmList() {

            viewModelScope.launch {


                    val url = /*TODO: DATA DINAMICA*/
                        "https://api.themoviedb.org/3/discover/movie?include_adult=true&include_video=false&language=it&page=1&primary_release_date.gte=2024-04-17&region=it&sort_by=popularity.desc&with_release_type=3"
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
            val title = filmObject.getString("title")
            val posterPath = filmObject.getString("poster_path")
            val plot = filmObject.getString("overview")
            val voteAverage = filmObject.getDouble("vote_average").toInt()
            val releaseDate = filmObject.getString("release_date")




            // Ottieni la data di due mesi fa
            val dueMesiFa = LocalDate.now().minusMonths(2)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val releaseDateLocalDate = LocalDate.parse(releaseDate, formatter)


            val film = Film(title, posterPath, plot, voteAverage, releaseDate)
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



