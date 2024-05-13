package com.example.cinevote.screens.settings

import androidx.lifecycle.ViewModel
import com.example.cinevote.screens.login.LoginState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsStatus(val name:String, val surname:String)

interface SettingsAction {
    fun setName():String
}

class SettingsVm : ViewModel() {

    private val _state= MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    val actions = object :  SettingsAction{
        override fun setName():String {

            val auth = FirebaseAuth.getInstance()
            return auth.currentUser?.displayName?:""
        }

    }
}