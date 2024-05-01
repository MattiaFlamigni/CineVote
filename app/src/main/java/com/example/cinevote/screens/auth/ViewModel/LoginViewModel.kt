package com.example.cinevote.screens.auth.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Loginstate(val username:String="", val password:String="")

interface LoginAction{
    fun setUsername(username:String)
    fun setPassword (password:String)
    fun checkKey(username:String, password: String): Boolean
}

class LoginViewModel: ViewModel() {

    private val _state = MutableStateFlow((Loginstate()))
    val state = _state.asStateFlow()


    val actions = object : LoginAction {

        override fun setUsername(username: String): Unit {
            _state.update { _state.value.copy(username = username) }
        }

        override fun setPassword(password: String) {
            _state.update { _state.value.copy(password = password) }
        }

        override fun checkKey(username: String, password: String): Boolean {
            return true
        }
    }
}