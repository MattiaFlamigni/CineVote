package com.example.cinevote.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.Film
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.util.TMDBService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeState(
    val topFilmList: List<Film> = emptyList(),
    val loadCommedia: List<Film> = emptyList(),
    val filmsByGenre: List<FilmList> = emptyList()
)

interface HomeAction{
    fun getTop()
    fun getFilmsByGenre(pages:Int, genre:Int)
}


class HomeVM(private val repository: FilmRepository) : ViewModel(){


    private val tmdb = TMDBService()
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    private val tmdbBaseUrl = "https://image.tmdb.org/t/p/w500"


    val action = object : HomeAction {
        override fun getTop() {


            viewModelScope.launch {


                val url = "https://api.themoviedb.org/3/movie/popular?language=it&region=it"
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

        override fun getFilmsByGenre(pages: Int, genre: Int) {


            viewModelScope.launch {
                repository.film.collect { filmList ->
                    val filmsWithBaseUrl = filmList.map { film ->
                        film.copy(posterPath = "$tmdbBaseUrl${film.posterPath}")
                    }
                    _state.value = _state.value.copy(filmsByGenre = filmsWithBaseUrl)
                }
            }










            /*viewModelScope.launch {
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
                                    updateFilmsByGenre(genre, allFilms)
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
            }*/
        }


    }



}