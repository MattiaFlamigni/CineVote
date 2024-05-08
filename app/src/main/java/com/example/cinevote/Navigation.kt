package com.example.cinevote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cinevote.screens.cinema.CinemaScreen
import com.example.cinevote.screens.details.DetailScreen
import com.example.cinevote.screens.HomeScreen
import com.example.cinevote.screens.OutNowScreen
import com.example.cinevote.screens.ReviewScreen
import com.example.cinevote.screens.signUp.SignUpGeneralScreen
import com.example.cinevote.screens.WishListScreen
import com.example.cinevote.screens.auth.AuthViewModel
import com.example.cinevote.screens.cinema.cinemaVm
import com.example.cinevote.screens.login.LoginScreen
import com.example.cinevote.screens.login.LoginViewModel
import com.example.cinevote.screens.signUp.SignUpMailScreen
import com.example.cinevote.screens.signUp.SignUpasswordScreen
import com.example.cinevote.screens.mainScreen
import com.example.cinevote.screens.searchScreen
import com.example.cinevote.screens.signUp.SignupViewModel
import org.koin.androidx.compose.koinViewModel


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

    data object Cinema : NavigationRoute("cinema")
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier =Modifier){

    val signUpVm = viewModel<(SignupViewModel)>()
    val signUpState by signUpVm.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Main.route, /*TODO*/
        modifier=modifier){

        composable(NavigationRoute.Login.route){
            //val LoginVm = viewModel<LoginViewModel>()
            val loginVm = koinViewModel<LoginViewModel>()
            val state by loginVm.state.collectAsState()
            val authVm = viewModel<AuthViewModel>()

            LoginScreen(navController = navController, state=state, actions = loginVm.actions, auth= authVm )
        }
        composable(NavigationRoute.SignUpGeneral.route){

            SignUpGeneralScreen(navController=navController, state = signUpState, actions=signUpVm.action)
        }
        composable(NavigationRoute.SignUpMail.route){
            SignUpMailScreen(navController=navController, state=signUpState, actions=signUpVm.action)
        }
        composable(NavigationRoute.SignUpPassword.route){
            SignUpasswordScreen(navController=navController, state = signUpState, actions = signUpVm.action)
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
        composable(NavigationRoute.Cinema.route){
            val cinemaViewModel = koinViewModel<cinemaVm>()
            val state by cinemaViewModel.state.collectAsState()

            CinemaScreen(navController = navController, state =state, /*actions = cinemaViewModel.actions::getCinema*/ action = cinemaViewModel.actions)
        }




    }
}