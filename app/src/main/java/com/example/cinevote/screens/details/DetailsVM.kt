package com.example.cinevote.screens.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.util.TMDBService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DetailState(
    val title:String="",
    val genres: String="" /*TODO*/,
    val plot: String="",
    val vote: Float ,
    val year: Int=0,
    val poster: String=""
)

interface DetailAction{
    fun showDetails(title:String)
    fun loadFromDb(title:String)
}

class DetailsVM(private val repository: FilmRepository) : ViewModel(){
    private val _state = MutableStateFlow(DetailState(vote = 0.00F))
    val state = _state.asStateFlow()
    private val tmdbBaseUrl = "https://image.tmdb.org/t/p/w500"
    private val tmdb = TMDBService()



    val action = object : DetailAction {
        override fun showDetails(title: String) {
            viewModelScope.launch {

                try {
                    val filmDetails = repository.getFilmFromTitle(title)



                val year = filmDetails.releaseDate.substring(0, 4)




                _state.value = _state.value.copy(
                    title = filmDetails.title,
                    genres = filmDetails.genreIDs, // TODO: Converti IDs in nomi dei generi se necessario
                    plot = filmDetails.plot,
                    vote = String.format("%.1f", filmDetails.voteAverage).replace(",", ".").toFloat(),
                    poster = "$tmdbBaseUrl${filmDetails.posterPath}",
                    year = year.toInt()
                )
                }catch (_:Exception){
                    loadFromDb(title)
                }
            }
        }

        override fun loadFromDb(title: String) {

            Log.d("recupero", title)
            tmdb.fetchFilmData(
                url ="https://api.themoviedb.org/3/search/movie?query=$title&region=it&language=it",
                onSuccess = {filmList->

                    Log.d("successo", filmList.size.toString())

                    filmList.forEach { filmData ->

                        val film = FilmList(
                            title = filmData.title,
                            posterPath =filmData.posterPath,
                            plot = filmData.plot,
                            voteAverage =filmData.voteAverage,
                            releaseDate =filmData.releaseDate,
                            genreIDs = filmData.genreIDs.toString()

                        )

                        Log.d("filmlist", film.toString())

                        viewModelScope.launch {
                            withContext(Dispatchers.IO) {
                                repository.upsert(film)
                            }
                        }
                    }

                },
                onFailure = {}
            )
        }
    }
}