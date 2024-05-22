package com.example.cinevote.screens.details

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.screens.home.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailState(
    val title:String="",
    val genres: String="" /*TODO*/,
    val plot : String="",
    val vote: Int=0,
    val year: Int=0,
    val poster: String=""
)

interface DetailAction{
    fun showDetails(title:String)
}

class DetailsVM(private val repository: FilmRepository) : ViewModel(){
    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()
    private val tmdbBaseUrl = "https://image.tmdb.org/t/p/w500"



    val action = object : DetailAction {
        override fun showDetails(title: String) {
            viewModelScope.launch {
                val filmDetails = repository.getFilmFromTitle(title)

                val year = filmDetails.releaseDate.substring(0,4)

                _state.value = _state.value.copy(
                    title = filmDetails.title,
                    genres = filmDetails.genreIDs, // TODO: Converti IDs in nomi dei generi se necessario
                    plot = filmDetails.plot,
                    vote = filmDetails.voteAverage,
                    poster = "$tmdbBaseUrl${filmDetails.posterPath}",
                    year = year.toInt()
                )
            }
        }
    }
}