package com.example.cinevote.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.cinevote.data.Film
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.screens.review.ReviewState
import com.example.cinevote.util.TMDBService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


data class SearchStatus(
    val titleToSearch : String="",
    val listfilm : List<Film> = emptyList()
)


interface searchAction{
    fun searchFilmByTitle(title:String)
}

class SearchVM() : ViewModel() {
    private val prefix = "https://api.themoviedb.org/3/search/movie?query="
    val suffix = "&include_adult=true&language=it&page=1"
    private val tmdb = TMDBService()
    private val _state = MutableStateFlow(SearchStatus())
    val state = _state.asStateFlow()


    val action = object : searchAction {
        override fun searchFilmByTitle(title: String) {
            val query = prefix+title+suffix
            tmdb.fetchFilmData(url = query,
                onSuccess = {film->
                _state.value = state.value.copy(listfilm = film)
                    Log.d("film trovati", state.value.listfilm.toString())
            },
                onFailure = {}
            )
        }

    }


}