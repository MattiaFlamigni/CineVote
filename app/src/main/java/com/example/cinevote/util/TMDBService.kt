package com.example.cinevote.util


import android.util.Log
import com.example.cinevote.data.Actors
import com.example.cinevote.data.Film
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import org.json.JSONException
import org.json.JSONObject

class TMDBService {

    // Definisci una funzione che accetta un URL e un callback per la gestione dei dati
    fun fetchFilmData(url: String, onSuccess: (List<Film>) -> Unit, onFailure: (IOException) -> Unit) {
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMTNhOGQwMGFhYjU1MDIwN2FlMDBiMDliZDBlNDIxMyIsInN1YiI6IjY1ZjcxZWZkMjQyZjk0MDE3ZGNjZjQxNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ._wgFHK2BtHQEPT_EHs1T6sfwVtxLscm2NKYAlOzRCfo")
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body
                    if (body != null) {
                        val jsonData = body.string()
                        try {
                            val filmList = parseFilmData(jsonData)
                            Log.d("Dimensioni", filmList.size.toString())
                            onSuccess(filmList)
                        } catch (e: Exception) {
                            // Gestisci eventuali errori nell'analisi dei dati JSON
                            onFailure(IOException(e))
                        }
                    } else {
                        // Il corpo della risposta è vuoto
                        onFailure(IOException("Response body is null"))
                    }
                } else {
                    // La richiesta non è stata eseguita con successo
                    onFailure(IOException("Request was not successful"))
                }
            }
        })
    }


    fun fetchActorFromFilmID(url: String, onSuccess: (List<Actors>) -> Unit, onFailure: (IOException) -> Unit) {
        val request: Request = Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMTNhOGQwMGFhYjU1MDIwN2FlMDBiMDliZDBlNDIxMyIsInN1YiI6IjY1ZjcxZWZkMjQyZjk0MDE3ZGNjZjQxNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ._wgFHK2BtHQEPT_EHs1T6sfwVtxLscm2NKYAlOzRCfo")
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body
                    if (body != null) {
                        val jsonData = body.string()
                        try {
                            val actors = parseActorData(jsonData)
                            onSuccess(actors)
                        } catch (e: Exception) {
                            // Gestisci eventuali errori nell'analisi dei dati JSON
                            onFailure(IOException(e))
                        }
                    } else {
                        // Il corpo della risposta è vuoto
                        onFailure(IOException("Response body is null"))
                    }
                } else {
                    // La richiesta non è stata eseguita con successo
                    onFailure(IOException("Request was not successful"))
                }
            }
        })
    }


}


private fun parseFilmData(jsonData: String): List<Film> {
    val filmList = mutableListOf<Film>()
    try {
        val jsonObject = JSONObject(jsonData)
        val resultsArray = jsonObject.getJSONArray("results")
        for (i in 0 until resultsArray.length()) {
            val filmObject = resultsArray.getJSONObject(i)
            val filmId = filmObject.getInt("id")
            val title = filmObject.getString("title")
            val posterPath = filmObject.getString("poster_path")
            val plot = filmObject.getString("overview")
            val voteAverage = filmObject.getDouble("vote_average").toFloat()
            val releaseDate = filmObject.getString("release_date")
            val genreIDsArray = filmObject.getJSONArray("genre_ids")
            val genreIDs = mutableListOf<Int>()
            for (i in 0 until genreIDsArray.length()) {
                val genreID = genreIDsArray.getInt(i)
                genreIDs.add(genreID)
            }


                val film = Film(filmId, title, posterPath, plot, voteAverage, releaseDate, genreIDs)
                filmList.add(film)
        }
    } catch (e: JSONException) {
        // Gestisci l'eccezione se ci sono problemi nell'analisi dei dati JSON
        Log.e("OutNowVM", "Errore nell'analisi dei dati JSON: ${e.message}")
    }
    return filmList
}


private fun parseActorData(jsonData: String): List<Actors> {
    val actors = mutableListOf<Actors>()
    try {
        val jsonObject = JSONObject(jsonData)
        val castArray = jsonObject.getJSONArray("cast")
        for (i in 0 until castArray.length()) {
            val actorObj = castArray.getJSONObject(i)
            val actorID = actorObj.getInt("id")
            val profilePath = actorObj.getString("profile_path")
            val name = actorObj.getString("name")
            val job = actorObj.getString("character")

            val actor = Actors(
                id = actorID,
                profilePic = profilePath,
                job = job,
                name = name
            )
            actors.add(actor)
        }
    } catch (e: JSONException) {
        // Gestisci l'eccezione se ci sono problemi nell'analisi dei dati JSON
        Log.e("OutNowVM", "Errore nell'analisi dei dati JSON: ${e.message}")
    }
    return actors
}


