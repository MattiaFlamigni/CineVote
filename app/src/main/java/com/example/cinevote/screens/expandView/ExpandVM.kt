package com.example.cinevote.screens.expandView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.util.TMDBService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class ExpandStatus(val extendedFilmList: List<FilmList> = emptyList())


interface ExpandActions {
    fun getFilm(genre: Int, pages: Int)
}

class ExpandVM(private val repository: FilmRepository) : ViewModel() {
    private val tmdb = TMDBService()

    private val _state = MutableStateFlow(ExpandStatus())
    val state = _state.asStateFlow()
    private val tmdbBaseUrl = "https://image.tmdb.org/t/p/w500"

    val action = object : ExpandActions {
        override fun getFilm(genre: Int, pages: Int) {
            viewModelScope.launch {
                repository.film.collect { filmList ->
                    val filteredFilms = filmList.filter { film ->
                        // Trasforma la stringa di generi in una lista di interi e controlla se contiene il genere desiderato
                        val genres = film.genreIDs.split(",")
                            .mapNotNull {
                                it.trim().toIntOrNull()
                            } // Pulisci la stringa e converti in Int
                        genre in genres


                    }.map { film ->


                        film.copy(posterPath = "$tmdbBaseUrl${film.posterPath}")
                    }
                    _state.value = _state.value.copy(extendedFilmList = filteredFilms)
                }
            }


            /*viewModelScope.launch {
                try {
                    val allFilms = mutableListOf<Film>()
                    for (page in 1..pages) {
                        val url =
                            "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=$page&sort_by=popularity.desc"
                        Log.d("FilmViewModel", "Request URL: $url")

                        tmdb.fetchFilmData(
                            url,
                            onSuccess = { filmList ->
                                allFilms.addAll(filmList)
                                if (page == pages) {
                                    _state.value = _state.value.copy(extendedFilmList = allFilms)
                                }
                            },
                            onFailure = { error ->
                                Log.e("FilmViewModel", "Errore nella richiesta: $error")
                            }
                        )
                    }
                } catch (e: Exception) {
                    Log.e("FilmViewModel", "Eccezione nella coroutine: ${e.message}", e)
                }*/
        }
    }
}
