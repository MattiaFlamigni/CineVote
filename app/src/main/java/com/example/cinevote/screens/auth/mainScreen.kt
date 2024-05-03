package com.example.cinevote.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.cinevote.screens.auth.viewModel.AuthState
import com.example.cinevote.screens.auth.viewModel.AuthStatus

@Composable
fun mainScreen(navController:NavHostController, authStateFlow: StateFlow<AuthState>){

    val authState by authStateFlow.collectAsState()

    when (authState.status) {
        AuthStatus.SIGNUP -> navController.navigate(NavigationRoute.SignUpGeneral.route)
        AuthStatus.LOGIN -> navController.navigate(NavigationRoute.Login.route)
        AuthStatus.LOGGED -> navController.navigate(NavigationRoute.HomeScreen.route)
    }

}