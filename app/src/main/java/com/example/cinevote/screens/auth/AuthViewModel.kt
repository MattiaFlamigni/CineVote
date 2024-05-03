package com.example.cinevote.screens.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow



data class AuthState(val status: AuthStatus)

class AuthViewModel :ViewModel() {
    private val _state = MutableStateFlow(AuthState(AuthStatus.SIGNUP))
    val state = _state.asStateFlow()


    fun changeState(authState: AuthStatus){
        _state.value = AuthState(authState)
    }
}