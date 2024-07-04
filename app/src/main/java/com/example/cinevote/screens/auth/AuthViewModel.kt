package com.example.cinevote.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.screens.settings.theme.model.Theme
import com.example.cinevote.screens.settings.theme.repositories.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(val status: AuthStatus, val isLoading: Boolean = true)

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(AuthState(AuthStatus.SIGNUP, isLoading = true))
    val state = _state.asStateFlow()

    init {
        loadState()
    }

    fun changeState(authState: AuthStatus) = viewModelScope.launch {
        repository.setTheme(authState)
        _state.value = AuthState(authState, isLoading = false)
    }

    private fun loadState() = viewModelScope.launch {
        try {
            val authStatus = repository.getAuthStatus()
            _state.value = AuthState(authStatus, isLoading = false)
            Log.d("AuthViewModel", "Loaded auth state: $authStatus")
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Error loading auth state", e)
            _state.value = AuthState(AuthStatus.SIGNUP, isLoading = false)
        }
    }
}
