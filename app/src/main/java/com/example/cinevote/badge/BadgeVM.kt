package com.example.cinevote.badge

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.R
import com.example.cinevote.data.Review
import com.example.cinevote.data.database.Firestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Badge(
    val name: String,
    val description: String,
    val image: Int,
    val type: BadgeType,
    val condition: String,
    val effect: String? = null
)

enum class BadgeType {
    WATCH_MOVIES, WRITE_REVIEWS, EARN_POINTS,
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
            viewModelScope.launch {
                var username = firestore.actions.getDataFromMail("username")

                if (username.isEmpty()) {
                    username = auth.currentUser?.displayName.toString()
                }

                // Implementa la logica per verificare se un badge è stato raggiunto
                // Esempio: controlla se l'utente ha soddisfatto la condizione del badge
                val isReached = when (badge.type) {
                    BadgeType.WATCH_MOVIES -> {
                        Log.d("username", username)
                        firestore.actions.getReviewByUser(username).size >= 10
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

                    BadgeType.EARN_POINTS -> {
                        // Logica per controllare se l'utente ha guadagnato i punti richiesti
                        false // Esempio di logica fittizia
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
                    description = "Guarda 10 film",
                    image = R.drawable.star,
                    type = BadgeType.WATCH_MOVIES,
                    condition = "Guarda 10 film",
                    effect = "Ottieni un badge Film Lover"
                ), Badge(
                    name = "Critico",
                    description = "Scrivi 5 recensioni",
                    image = R.drawable.star,
                    type = BadgeType.WRITE_REVIEWS,
                    condition = "Scrivi 5 recensioni",
                    effect = "Ottieni un badge Critico"
                )
                // Aggiungi altri badge qui
            )
            _state.value = _state.value.copy(badgeList = badges)
        }
    }
}
