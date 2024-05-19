package com.example.cinevote.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.Film
import com.example.cinevote.screens.outNow.OutNowStatus
import com.example.cinevote.util.TMDBService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeState(
    val topFilmList: List<Film> = emptyList(),
    val loadCommedia: List<Film> = emptyList(),
    val filmsByGenre: Map<Int, List<Film>> = emptyMap()
)

interface HomeAction{
    fun getTop()
    fun getFilmsByGenre(page:Int, genre:Int)
}


class HomeVM : ViewModel(){


    private val tmdb = TMDBService()
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()



    val action = object : HomeAction {
        override fun getTop() {


            viewModelScope.launch {


                val url = "https://api.themoviedb.org/3/movie/popular?language=en-US&page=1"
                tmdb.fetchFilmData(
                    url,
                    onSuccess = { filmList ->
                        _state.update { it.copy(topFilmList = filmList) }
                    },
                    onFailure = {
                        Log.e("OutNowVM", "Errore nella richiesta")
                    }
                )
            }
        }

        override fun getFilmsByGenre(page: Int, genre: Int) {
            viewModelScope.launch {
                try {
                    val url =
                        "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=$page&sort_by=popularity.desc&with_genres=$genre"
                    Log.d("FilmViewModel", "Request URL: $url")

                    tmdb.fetchFilmData(
                        url,
                        onSuccess = { filmList ->
                            updateFilmsByGenre(genre, filmList)
                        },
                        onFailure = { error ->
                            Log.e("FilmViewModel", "Errore nella richiesta: $error")
                        }
                    )
                } catch (e: Exception) {
                    Log.e("FilmViewModel", "Eccezione nella coroutine: ${e.message}", e)
                }
            }
        }

    }


    private fun updateFilmsByGenre(genre: Int, newFilms: List<Film>) {
        _state.update { currentState ->
            val updatedMap = currentState.filmsByGenre.toMutableMap()
            val existingFilms = updatedMap[genre] ?: emptyList()
            updatedMap[genre] = existingFilms + newFilms
            currentState.copy(filmsByGenre = updatedMap)
        }
    }

}