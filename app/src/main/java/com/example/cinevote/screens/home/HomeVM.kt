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
    val otherFilm: List<Film> = emptyList()
)

interface HomeAction{
    fun getTop()
    fun getAll(page:Int)
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

        override fun getAll(page:Int) {
            val url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=it&page=$page&region=it&sort_by=popularity.desc"

            tmdb.fetchFilmData(
                url,
                onSuccess = { filmList ->
                    _state.update { it.copy(otherFilm = filmList) }
                },
                onFailure = {
                    Log.e("OutNowVM", "Errore nella richiesta")
                }
            )
        }

    }

}