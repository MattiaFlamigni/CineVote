package com.example.cinevote

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cinevote.screens.DetailScreen
import com.example.cinevote.screens.HomeScreen
import com.example.cinevote.screens.OutNowScreen
import com.example.cinevote.screens.ReviewScreen
import com.example.cinevote.screens.SignUpGeneralScreen
import com.example.cinevote.screens.WishListScreen
import com.example.cinevote.screens.auth.ViewModel.AuthViewModel
import com.example.cinevote.screens.auth.LoginScreen
import com.example.cinevote.screens.auth.ViewModel.LoginViewModel
import com.example.cinevote.screens.auth.SignUpMailScreen
import com.example.cinevote.screens.auth.SignUpasswordScreen
import com.example.cinevote.screens.auth.mainScreen
import com.example.cinevote.screens.searchScreen

sealed class NavigationRoute(val route:String){
    data object Login : NavigationRoute("Login")
    data object SignUpGeneral : NavigationRoute("SignUpGeneral")
    data object SignUpMail : NavigationRoute("SignUpMail")
    data object SignUpPassword : NavigationRoute("SignUpPassword")
    data object HomeScreen : NavigationRoute("Home")
    data object WishList : NavigationRoute("wishList")
    data object OutNow : NavigationRoute("outNow")
    data object Review : NavigationRoute("review")

    data object Ricerca : NavigationRoute("cerca")
    data object Detail : NavigationRoute("detail")
    data object Main : NavigationRoute("main")


}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier =Modifier){
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Main.route, /*TODO*/
        modifier=modifier){

        composable(NavigationRoute.Login.route){
            val LoginVm = viewModel<LoginViewModel>()
            val state = LoginVm.state
            LoginScreen(navController = navController, LoginVm, {}  )
        }
        composable(NavigationRoute.SignUpGeneral.route){
            SignUpGeneralScreen(navController=navController)
        }
        composable(NavigationRoute.SignUpMail.route){
            SignUpMailScreen(navController=navController)
        }
        composable(NavigationRoute.SignUpPassword.route){
            SignUpasswordScreen(navController=navController)
        }
        composable(NavigationRoute.HomeScreen.route){
            HomeScreen(navController=navController)
        }
        composable(NavigationRoute.WishList.route){
            WishListScreen(navController = navController)
        }
        composable(NavigationRoute.OutNow.route){
            OutNowScreen(navController = navController)
        }
        composable(NavigationRoute.Review.route){
            ReviewScreen(navController = navController)
        }

        composable(NavigationRoute.Ricerca.route){
            searchScreen(navController = navController)
        }

        composable(NavigationRoute.Detail.route){
            DetailScreen(navController = navController)
        }

        composable(NavigationRoute.Main.route){
            val authviewModel = viewModel<AuthViewModel>()
            mainScreen(navController = navController, authviewModel.state)
        }




    }
}