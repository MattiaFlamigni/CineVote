package com.example.cinevote.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinevote.data.database.Firestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsStatus(
    val name:String="",
    val surname:String="",
    val username:String=""
)

interface SettingsAction {
    fun logOut()
}

class SettingsVm : ViewModel() {

    val firestore = Firestore()
    val auth = FirebaseAuth.getInstance()

    private val _state= MutableStateFlow(SettingsStatus())
    val state = _state.asStateFlow()

    init{
        fetchUserData()
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            val username = firestore.actions.getDataFromMail("username")
            val name = auth.currentUser?.displayName?:"Utente non registrato"
            _state.update { it.copy(username=username, name = name) }
        }
    }

    val action = object : SettingsAction {
        override fun logOut() {
            auth.signOut()

        }

    }







}

