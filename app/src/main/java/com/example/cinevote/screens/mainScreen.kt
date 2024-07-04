package com.example.cinevote.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinevote.screens.auth.AuthRepository
import com.example.cinevote.screens.auth.AuthState
import com.example.cinevote.screens.auth.AuthStatus
import com.example.cinevote.screens.auth.AuthViewModel

@Composable
fun mainScreen(navController: NavHostController, authViewModel: AuthViewModel = viewModel()) {
    val authState by authViewModel.state.collectAsState()

    Log.d("statoooooo", authState.status.toString())

    // Mostra uno schermo di caricamento o una schermata di attesa finché il caricamento non è completato
    if (authState.isLoading) {
        // Mostra una schermata di caricamento o attesa
         // Puoi sostituirlo con una schermata di attesa personalizzata
    } else {
        when (authState.status) {
            AuthStatus.SIGNUP -> navController.navigate(NavigationRoute.SignUpGeneral.route)
            AuthStatus.LOGIN -> navController.navigate(NavigationRoute.Login.route)
            AuthStatus.LOGGED -> navController.navigate(NavigationRoute.HomeScreen.route)
        }
    }
}
