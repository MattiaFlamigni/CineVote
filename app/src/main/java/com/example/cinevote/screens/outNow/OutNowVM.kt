package com.example.cinevote.screens.outNow

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.cinevote.screens.cinema.Cinema
import java.time.LocalDate
import com.example.cinevote.screens.cinema.DISTANCE

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

    private val _state = MutableStateFlow(OutNowStatus())
    val state = _state.asStateFlow()

    val action = object : FilmAction {
        override fun getFilmList() {

            //val url="https://overpass-api.de/api/interpreter?data=[out:json];node[amenity=cinema](around:100000,40.7128,-74.00);out;"
            val request: okhttp3.Request = okhttp3.Request.Builder()
                .url("https://api.themoviedb.org/3/movie/now_playing?language=it&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMTNhOGQwMGFhYjU1MDIwN2FlMDBiMDliZDBlNDIxMyIsInN1YiI6IjY1ZjcxZWZkMjQyZjk0MDE3ZGNjZjQxNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ._wgFHK2BtHQEPT_EHs1T6sfwVtxLscm2NKYAlOzRCfo")
                .build()


            val client = okhttp3.OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Gestisci il fallimento della richiesta
                }

                override fun onResponse(call: Call, response: Response) {

                    if (response.isSuccessful) {
                        val body = response.body
                        if (body != null) {
                            val jsonData = body.string()
                            // Analizza i dati JSON ricevuti e aggiorna lo stato
                            try {
                                // Esempio di analisi dei dati JSON (dipende dalla struttura dei dati)
                                val filmList = parseFilmData(jsonData)
                                _state.update { it.copy(filmList = filmList) }
                            } catch (e: Exception) {
                                // Gestisci eventuali errori nell'analisi dei dati JSON
                            }
                        } else {
                            // Il corpo della risposta è vuoto
                            // Gestisci questa situazione di conseguenza
                        }
                    } else {
                        // La richiesta non è stata eseguita con successo
                        // Gestisci questa situazione di conseguenza
                    }
                }
            })
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



