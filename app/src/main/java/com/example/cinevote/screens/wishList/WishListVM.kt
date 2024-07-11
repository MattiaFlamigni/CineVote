package com.example.cinevote.screens.wishList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.Film
import com.example.cinevote.data.database.Room.FilmList
import com.example.cinevote.data.repository.FilmRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


data class wishListState(
    val favoriteFilmList: List<FilmList> = emptyList()
)

interface WishListAction {
    fun loadFilm(mail: String)
}

class WishListVM(private val repository: FilmRepository) : ViewModel() {
    private val _state = MutableStateFlow(wishListState())
    val state = _state.asStateFlow()
    private val tmdbBaseUrl = "https://image.tmdb.org/t/p/w500"
    val db = FirebaseFirestore.getInstance()


    val action = object : WishListAction {
        override fun loadFilm(mail: String) {
            val tmpList: MutableList<String> = mutableListOf()
            val filmList: MutableList<FilmList> = mutableListOf()
            val prova: Film


            viewModelScope.launch {
                val query = db.collection("favorites").whereEqualTo("user", mail).get().await()
                for (document in query.documents) {
                    val title = document.getString("title")
                    title?.let { it ->
                        tmpList.add(it)
                    }

                }

                for (filmTitle in tmpList) {
                    val film = repository.getFilmFromTitle(filmTitle)
                    filmList.add(film)

                }

                _state.value = state.value.copy(filmList)


                Log.d("listadifilmfavoriti", state.value.favoriteFilmList.toString())

            }


        }
    }

}