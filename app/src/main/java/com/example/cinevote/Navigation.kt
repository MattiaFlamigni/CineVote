package com.example.cinevote

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cinevote.screens.cinema.CinemaScreen
import com.example.cinevote.screens.details.DetailScreen
import com.example.cinevote.screens.home.HomeScreen
import com.example.cinevote.screens.outNow.OutNowScreen
import com.example.cinevote.screens.review.ReviewScreen
import com.example.cinevote.screens.settings.SettingsScreen
import com.example.cinevote.screens.signUp.SignUpGeneralScreen
import com.example.cinevote.screens.wishList.WishListScreen
import com.example.cinevote.screens.auth.AuthViewModel
import com.example.cinevote.screens.cinema.cinemaVm
import com.example.cinevote.screens.details.DetailsVM
import com.example.cinevote.screens.expandView.ExpandScreen
import com.example.cinevote.screens.expandView.ExpandVM
import com.example.cinevote.screens.home.HomeVM
import com.example.cinevote.screens.login.LoginScreen
import com.example.cinevote.screens.login.LoginViewModel
import com.example.cinevote.screens.signUp.SignUpMailScreen
import com.example.cinevote.screens.signUp.SignUpasswordScreen
import com.example.cinevote.screens.mainScreen
import com.example.cinevote.screens.outNow.OutNowVM
import com.example.cinevote.screens.review.reviewVM
import com.example.cinevote.screens.search.SearchVM
import com.example.cinevote.screens.search.searchScreen
import com.example.cinevote.screens.settings.SettingsVm
import com.example.cinevote.screens.settings.ThemeScreen
import com.example.cinevote.screens.settings.ThemeViewModel
import com.example.cinevote.screens.signUp.SignupViewModel
import com.example.cinevote.screens.wishList.WishListVM
import org.koin.androidx.compose.koinViewModel


sealed class NavigationRoute(val route:String, val arguments: List<NamedNavArgument> = emptyList()){
    data object Login : NavigationRoute("Login")
    data object SignUpGeneral : NavigationRoute("SignUpGeneral")
    data object SignUpMail : NavigationRoute("SignUpMail")
    data object SignUpPassword : NavigationRoute("SignUpPassword")
    data object HomeScreen : NavigationRoute("Home")
    data object WishList : NavigationRoute("wishList")

    data object ThemeScreen : NavigationRoute("theme")
    data object OutNow : NavigationRoute("outNow")
    //data object Review : NavigationRoute("review")
    data object Review : NavigationRoute("review/{title}", listOf(navArgument("title") { type = NavType.StringType })) {
        fun buildRoute(title: String) = "review/$title"
    }

    data object Ricerca : NavigationRoute("cerca")
    //data object Detail : NavigationRoute("detail")

    data object Detail : NavigationRoute("detail/{title}", listOf(navArgument("title") { type = NavType.StringType })) {
        fun buildRoute(title: String) = "detail/$title"
    }
    data object Main : NavigationRoute("main")
    data object Cinema : NavigationRoute("cinema")
    data object Settings : NavigationRoute("impostazioni")
    data object Expand : NavigationRoute("expand/{genreid}", listOf(navArgument("genreid") { type = NavType.IntType })) {
        fun buildRoute(genreid: Int) = "expand/$genreid"
    }

    //data object Expand : NavigationRoute("expan")

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
            val authVm = koinViewModel<AuthViewModel>()
            val authState by authVm.state.collectAsState()

            LoginScreen(navController = navController, state=state, actions = loginVm.actions, authViewModel = authVm, status = authState )
        }
        composable(NavigationRoute.SignUpGeneral.route){

            SignUpGeneralScreen(navController=navController, state = signUpState, actions=signUpVm.action)
        }
        composable(NavigationRoute.SignUpMail.route){
            SignUpMailScreen(navController=navController, state=signUpState, actions=signUpVm.action)
        }
        composable(NavigationRoute.SignUpPassword.route){
            //val authVm =koinViewModel<AuthViewModel>()
            SignUpasswordScreen(navController=navController, state = signUpState, actions = signUpVm.action)
        }
        composable(NavigationRoute.HomeScreen.route){
            val homeVM = koinViewModel<HomeVM>()
            val state by homeVM.state.collectAsState()
            HomeScreen(navController =navController, state = state, actions = homeVM.action)
        }
        composable(NavigationRoute.Settings.route){
            val settingsVm = koinViewModel<SettingsVm>()
            val state by settingsVm.state.collectAsState()
            val authviewModel = koinViewModel<AuthViewModel>()
            SettingsScreen(navController=navController, state= state, action = settingsVm.action, auth = authviewModel)
        }
        composable(NavigationRoute.WishList.route){
            val wishListVM = koinViewModel<WishListVM>()
            val statee by wishListVM.state.collectAsState()
            WishListScreen(navController = navController, state = statee, action = wishListVM.action)
        }

        composable(NavigationRoute.ThemeScreen.route){
            val themeVM = koinViewModel<ThemeViewModel>()
            val state by themeVM.state.collectAsState()
            ThemeScreen(state = state, themeVM::changeTheme )
        }

        composable(NavigationRoute.OutNow.route){
            val outNowVm = koinViewModel<OutNowVM>()
            val state by outNowVm.state.collectAsState()
            OutNowScreen(navController = navController,state=state, action = outNowVm.action)
        }


        composable(NavigationRoute.Ricerca.route){
            val searchVM = koinViewModel<SearchVM>()
            val state by searchVM.state.collectAsState()
            searchScreen(navController = navController, state = state, action = searchVM.action )
        }

        /*composable(NavigationRoute.Detail.route){
            DetailScreen(navController = navController)
        }*/



        composable(
            route = NavigationRoute.Review.route,
            arguments= listOf(navArgument("title") { type = NavType.StringType })
        ){backStackEntry ->
            val settingsVm = koinViewModel<reviewVM>()
            val state by settingsVm.state.collectAsState()
            val title = backStackEntry.arguments?.getString("title") ?: ""
            ReviewScreen(navController = navController, state = state, action = settingsVm.action, title =title)
        }


        composable(
            route = NavigationRoute.Detail.route,
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            val detailVm = koinViewModel<DetailsVM>()
            val state by detailVm.state.collectAsState()

            val title = backStackEntry.arguments?.getString("title") ?: ""
            Log.d("ExpandScreen", "Genre ID: $title")  // Debug log

            DetailScreen(navController = navController, action = detailVm.action, title = title, state = state)
        }


        /*composable(NavigationRoute.Expand.route) {
            val expand = koinViewModel<ExpandVM>()
            val state by expand.state.collectAsState()
            //ExpandScreen(navController = navController,state=state, action = expand.action, 28)
        }*/

        composable(
            route = NavigationRoute.Expand.route,
            arguments = listOf(navArgument("genreid") { type = NavType.IntType })
        ) { backStackEntry ->
            val expandVM = koinViewModel<ExpandVM>()
            val state by expandVM.state.collectAsState()

            val genreId = backStackEntry.arguments?.getInt("genreid") ?: 0
            Log.d("ExpandScreen", "Genre ID: $genreId")  // Debug log

            ExpandScreen(navController = navController, action = expandVM.action, genre = genreId, state = state)
        }

        composable(NavigationRoute.Main.route){
            val authviewModel = koinViewModel<AuthViewModel>()
            val state by authviewModel.state.collectAsState()
            mainScreen(navController = navController, authviewModel)
        }
        composable(NavigationRoute.Cinema.route){
            val cinemaViewModel = koinViewModel<cinemaVm>()
            val state by cinemaViewModel.state.collectAsState()

            CinemaScreen(navController = navController, state =state, action = cinemaViewModel.actions)
        }




    }
}