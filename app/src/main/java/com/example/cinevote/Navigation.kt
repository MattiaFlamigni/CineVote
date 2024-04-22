package com.example.cinevote

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cinevote.screens.LoginScreen
import com.example.cinevote.screens.SignUpForm
import com.example.cinevote.screens.SignUpScreen

sealed class NavigationRoute(val route:String){
    data object Login : NavigationRoute("Login")
    data object SignUp : NavigationRoute("SignUp")
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier =Modifier){
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Login.route,
        modifier=modifier){

        composable(NavigationRoute.Login.route){
            LoginScreen(navController = navController)
        }
        composable(NavigationRoute.SignUp.route){
            SignUpScreen(navController=navController)
        }


    }
}