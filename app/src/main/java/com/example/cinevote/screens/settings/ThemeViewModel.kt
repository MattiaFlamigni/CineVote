package com.example.cinevote.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.screens.settings.theme.model.Theme
import com.example.cinevote.screens.settings.theme.repositories.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ThemeState(val theme: Theme)

class ThemeViewModel(
    private val repository: ThemeRepository
) : ViewModel() {
    val state = repository.theme.map { ThemeState(it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ThemeState(Theme.System)
    )

    fun changeTheme(theme: Theme) = viewModelScope.launch {
        repository.setTheme(theme)
    }
}
