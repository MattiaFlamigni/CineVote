package com.example.cinevote.badge

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinevote.R
import com.example.cinevote.data.Review
import com.example.cinevote.data.database.Firestore
import com.example.cinevote.util.TMDBService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class Badge(
    val name: String,
    val description: String,
    val image: Int,
    val type: BadgeType,
    val condition: String,
    val effect: String? = null
)

enum class BadgeType {
    WATCH_MOVIES, WRITE_REVIEWS, EXPLORER,
    // Aggiungi altri tipi di badge qui
}


data class BadgeStatus(
    val badgeList: List<Badge> = emptyList(), val achievedBadges: List<Badge> = emptyList()
)

interface BadgeAction {
    fun checkBadgeReached(badge: Badge)
}

class BadgeViewModel : ViewModel() {
    private val _state = MutableStateFlow(BadgeStatus())
    val state = _state.asStateFlow()
    private val firestore = Firestore()
    private val auth = FirebaseAuth.getInstance()

    init {
        loadBadges()
    }


    val action = object : BadgeAction {


        override fun checkBadgeReached(badge: Badge) {
            viewModelScope.launch(Dispatchers.IO) {
                    val username = auth.currentUser?.email.toString()


                val isReached = when (badge.type) {
                    BadgeType.WATCH_MOVIES -> {
                        Log.d("username", username)
                        firestore.actions.getReviewByUser(username).size >= 2
                    }

                    BadgeType.WRITE_REVIEWS -> {
                        val list: MutableList<Review> = mutableListOf()
                        val reviews = firestore.actions.getReviewByUser(username)
                        for (review in reviews) {
                            if (review.descrizione.isNotEmpty()) {
                                list.add(review)
                            }
                        }

                        list.size >= 5
                    }

                    BadgeType.EXPLORER -> {
                        val tmdb = TMDBService()
                        val list: MutableList<String> = mutableListOf()
                        val firestore = Firestore()
                        val prefix = "https://api.themoviedb.org/3/search/movie?query="
                        val suffix = "&include_adult=true&language=it&page=1"
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                        val listTitle = firestore.actions.loadFavorites(auth.currentUser?.email ?: "")
                        for (film in listTitle) {
                            tmdb.fetchFilmData(
                                url = prefix + film + suffix,
                                onFailure = {},
                                onSuccess = { films ->
                                    for (film in films) {
                                        try {
                                            val date = LocalDate.parse(film.releaseDate, formatter)
                                            if (!date.isBefore(LocalDate.now())) {
                                                Log.d("sizeBadge", "Film trovato con data futura: ${film.title}")
                                                list.add(film.title)
                                            }
                                        } catch (e: DateTimeParseException) {
                                            Log.e("BadgeViewModel", "Errore parsing data: ${e.message}")
                                        }
                                    }

                                    Log.d("sizeBadge", "Dimensione lista: ${list.size}")
                                    if (list.size >= 5) {
                                        Log.d("sizeBadge", "Badge raggiunto per EXPLORER")
                                    }
                                }
                            )
                        }

                        Log.d("sizeBadge", "Dimensione lista finale: ${list.size}")


                        delay(500)
                        list.size>5 //TODO: PERFORM!!!

                    }

                }

                if (isReached) {
                    val updatedAchievedBadges = _state.value.achievedBadges.toMutableList().apply {
                        if (!contains(badge)) {
                            add(badge)
                        }
                    }
                    _state.value = _state.value.copy(achievedBadges = updatedAchievedBadges)
                }

            }
        }
    }


    private fun loadBadges() {
        viewModelScope.launch {
            val badges = listOf(
                Badge(
                    name = "Film Lover",
                    description = "Guarda 2 film",
                    image = R.drawable.star,
                    type = BadgeType.WATCH_MOVIES,
                    condition = "Guarda 2 film",
                    effect = "Ottieni un badge Film Lover"
                ), Badge(
                    name = "Critico",
                    description = "Scrivi 5 recensioni",
                    image = R.drawable.star,
                    type = BadgeType.WRITE_REVIEWS,
                    condition = "Scrivi 5 recensioni",
                    effect = "Ottieni un badge Critico"
                ), Badge(
                    name = "Esploratore",
                    description = "Salva 5 film con uscita futura",
                    image = R.drawable.star,
                    type = BadgeType.EXPLORER,
                    condition = "Salva 5 film in uscita",
                    effect = "Ottieni un badge Esploratore"
                )
                // Aggiungi altri badge qui
            )
            _state.value = _state.value.copy(badgeList = badges)
        }
    }
}
