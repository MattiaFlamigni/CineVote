package com.example.cinevote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.cinevote.screens.auth.AuthRepository
import com.example.cinevote.screens.auth.AuthStatus
import com.example.cinevote.screens.auth.AuthViewModel
import com.example.cinevote.screens.login.LoginViewModel
import com.example.cinevote.screens.settings.ThemeViewModel
import com.example.cinevote.screens.settings.theme.model.Theme
import com.example.cinevote.screens.signUp.SignupViewModel
import com.example.cinevote.screens.signUp.firebaseAuth


import com.example.cinevote.ui.theme.CineVoteTheme
import com.example.cinevote.util.TMDBService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.androidx.compose.koinViewModel


class MainActivity() : ComponentActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)

        val tmdb = TMDBService()
        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("366608684761-oqr72k5ikfvrukgm1oummsnv45vsvs69.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()
        //signInGoogle()







        setContent {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()

            CineVoteTheme(darkTheme = when (themeState.theme) {
                Theme.Light -> false
                Theme.Dark -> true
                Theme.System -> isSystemInDarkTheme()
            }) {

                 /*darkTheme = when (themeState.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }*/



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
    fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        val viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {

                /*viewModel.action.createUser(name = firebaseAuth.currentUser?.displayName ?:"", mail = firebaseAuth.currentUser?.email
                    ?:"", password = " ", surname="", username = firebaseAuth.currentUser?.displayName ?:""  )*/
                UpdateUI(account)

            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("GOOGLE", FirebaseAuth.getInstance().currentUser?.email.toString())
            }
        }
    }
}


