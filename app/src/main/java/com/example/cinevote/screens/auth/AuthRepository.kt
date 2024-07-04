package com.example.cinevote.screens.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cinevote.screens.settings.theme.model.Theme
import kotlinx.coroutines.flow.first

import kotlinx.coroutines.flow.map


class AuthRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val AUTH_KEY = stringPreferencesKey("auth")
    }

    val theme = dataStore.data
        .map { preferences ->
            try {
                AuthStatus.valueOf(preferences[AUTH_KEY] ?: "")
            } catch (_: Exception) {
                AuthStatus.SIGNUP
            }
        }

    suspend fun setTheme(auth: AuthStatus) = dataStore.edit { it[AUTH_KEY] = auth.name }

    suspend fun getAuthStatus(): AuthStatus {
        val preferences = dataStore.data.first()
        return try {
            AuthStatus.valueOf(preferences[AUTH_KEY] ?: "")
        } catch (_: Exception) {
            AuthStatus.SIGNUP
        }
    }
}
