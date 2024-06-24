package com.example.cinevote.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.screens.details.DetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class ReviewState(
    val year : Int,
    val image : String = ""
)

interface ReviewAction{
    fun setYear(title:String)
}
class reviewVM(private val repository: FilmRepository): ViewModel() {

    private val tmdbBaseUrl = "https://image.tmdb.org/t/p/w500"
    private val _state = MutableStateFlow(ReviewState(0))
    val state = _state.asStateFlow()


    val action = object : ReviewAction{

        override fun setYear(title:String) {

            viewModelScope.launch {

                    val film = repository.getFilmFromTitle(title)
                    val year = film.releaseDate.substring(0,4)
                    _state.value = _state.value.copy(year=year.toInt(), image=tmdbBaseUrl+film.posterPath )
            }



        }

    }


}