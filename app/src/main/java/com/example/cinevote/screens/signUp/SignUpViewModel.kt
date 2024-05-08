package com.example.cinevote.screens.signUp

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SignupState(
    val name:String="",
    val surname:String="",
    val username:String="",
    val mail :String ="",
    val password:String=""
)

interface SignUPActions{
    fun setName(name:String)
    fun setSurname(surname:String)
    fun setUsername(username:String)
    fun setMail(mail:String)
    fun setPassword(password:String)
    fun createUser(
        name:String,
        surname:String,
        username:String,
        mail:String,
        password: String
    )
}
class SignupViewModel : ViewModel() {
    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()




    val action= object :SignUPActions{
        override fun setName(name: String) {
            _state.update { it.copy(name=name) }
        }

        override fun setSurname(surname: String) {
            _state.update { it.copy(surname=surname) }
        }

        override fun setUsername(username: String) {
            _state.update { it.copy(username=username) }
        }

        override fun setMail(mail: String) {
            _state.update { it.copy(mail=mail) }
        }

        override fun setPassword(password: String) {
            _state.update { it.copy(password=password) }
        }

        override fun createUser(
            name: String,
            surname: String,
            username: String,
            mail: String,
            password: String
        ) {

            /*TODO */
        }

    }
}