package com.example.cinevote.screens.expandView

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.Film
import com.example.cinevote.screens.outNow.OutNowStatus
import com.example.cinevote.util.TMDBService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ExpandStatus(val extendedFilmList : List<Film> = emptyList())


interface ExpandActions{
    fun getFilm(genre:Int, pages:Int)
}

class ExpandVM : ViewModel() {
    private val tmdb = TMDBService()

    private val _state = MutableStateFlow(ExpandStatus())
    val state = _state.asStateFlow()


    val action = object : ExpandActions {
        override fun getFilm(genre: Int, pages : Int) {
            viewModelScope.launch {
                try {
                    val allFilms = mutableListOf<Film>()
                    for (page in 1..pages) {
                        val url =
                            "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=$page&sort_by=popularity.desc&with_genres=$genre"
                        Log.d("FilmViewModel", "Request URL: $url")

                        tmdb.fetchFilmData(
                            url,
                            onSuccess = { filmList ->
                                allFilms.addAll(filmList)
                                if (page == pages) {
                                    _state.update { it.copy(extendedFilmList = allFilms) }
                                }
                            },
                            onFailure = { error ->
                                Log.e("FilmViewModel", "Errore nella richiesta: $error")
                            }
                        )
                    }
                } catch (e: Exception) {
                    Log.e("FilmViewModel", "Eccezione nella coroutine: ${e.message}", e)
                }
            }
        }

    }



}