package com.example.cinevote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController


import com.example.cinevote.ui.theme.CineVoteTheme
import com.example.cinevote.util.TMDBService
import com.jakewharton.threetenabp.AndroidThreeTen


class MainActivity() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)


        val tmdb = TMDBService()




        setContent {
            CineVoteTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    val navController = rememberNavController()
                    NavGraph(navController = navController)


                    val backCallback = remember {
                        object : OnBackPressedCallback(true) {
                            override fun handleOnBackPressed() {
                                if (navController.currentBackStackEntry?.destination?.route == NavigationRoute.HomeScreen.route) {
                                    finishAffinity()
                                } else {
                                    navController.navigateUp()
                                }
                            }
                        }
                    }
                    val context = LocalContext.current
                    DisposableEffect(Unit) {
                        val dispatcher = (context as ComponentActivity).onBackPressedDispatcher
                        dispatcher.addCallback(backCallback)

                        onDispose {
                            backCallback.remove()
                        }
                    }
                }
            }
        }
    }
}


