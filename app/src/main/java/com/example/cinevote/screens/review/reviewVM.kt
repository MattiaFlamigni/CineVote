package com.example.cinevote.screens.review

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinevote.data.repository.FilmRepository
import com.example.cinevote.screens.details.DetailState
import com.example.cinevote.screens.signUp.firebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


data class ReviewState(
    val year : Int,
    val rating : Int=0,
    val desc : String = "",
    val image : String = ""
)

interface ReviewAction{
    fun setYear(title:String)
    fun setRating(rating:Int)
    fun setDesc(desc:String)

    fun uploadReview(title:String)
}
class reviewVM(private val repository: FilmRepository): ViewModel() {

    private val tmdbBaseUrl = "https://image.tmdb.org/t/p/w500"
    private val _state = MutableStateFlow(ReviewState(0))
    private val db = FirebaseFirestore.getInstance()
    val state = _state.asStateFlow()


    val action = object : ReviewAction{

        override fun setYear(title:String) {

            viewModelScope.launch {

                    val film = repository.getFilmFromTitle(title)
                    val year = film.releaseDate.substring(0,4)
                    _state.value = _state.value.copy(year=year.toInt(), image=tmdbBaseUrl+film.posterPath )
            }
        }

        override fun setRating(rating: Int) {
            _state.value = _state.value.copy(rating=rating)
        }

        override fun setDesc(desc: String) {
            _state.value = _state.value.copy(desc=desc)
        }

        override fun uploadReview(title:String) {
            viewModelScope.launch {
                val mail = firebaseAuth.currentUser?.email
                val query = db.collection("users").whereEqualTo("mail", mail).get().await()
                val username = query.documents[0].getString("username")


                val review = hashMapOf(
                    "descrizione" to state.value.desc,
                    "autore" to username,
                    "stelle" to state.value.rating,
                    "titolo" to title
                )

                db.collection("review").add(review).addOnSuccessListener {
                    Log.d("firestore review", "Ok")
                }
            }
        }

    }


}