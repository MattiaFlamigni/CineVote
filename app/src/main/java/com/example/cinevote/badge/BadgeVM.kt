package com.example.cinevote.badge

import android.media.Image
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.cinevote.screens.settings.SettingsStatus
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.cinevote.R

data class Badge(
    val name: String,
    val description: String,
    val image: Int,
    val type: BadgeType,
    val condition: String,
    val effect: String? = null
)

enum class BadgeType {
    WATCH_MOVIES,
    WRITE_REVIEWS,
    EARN_POINTS,
    // Aggiungi altri tipi di badge qui
}


data class BadgeStatus(
    val badgeList: List<Badge> = emptyList(),
    val achievedBadges: List<Badge> = emptyList()
)

interface BadgeAction {
    fun checkBadgeReached(badge: Badge): Boolean
}

class BadgeViewModel : ViewModel() {
    private val _state = MutableStateFlow(BadgeStatus())
    val state = _state.asStateFlow()

    init {
        loadBadges()
    }


    val action = object : BadgeAction {


        override fun checkBadgeReached(badge: Badge): Boolean {
            // Implementa la logica per verificare se un badge è stato raggiunto
            // Esempio: controlla se l'utente ha soddisfatto la condizione del badge
            val isReached = when (badge.type) {
                BadgeType.WATCH_MOVIES -> {
                    // Logica per controllare se l'utente ha guardato il numero di film richiesti
                    true // Esempio di logica fittizia
                }

                BadgeType.WRITE_REVIEWS -> {
                    // Logica per controllare se l'utente ha scritto il numero di recensioni richieste
                    false // Esempio di logica fittizia
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
            return isReached
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
                ),
                Badge(
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
