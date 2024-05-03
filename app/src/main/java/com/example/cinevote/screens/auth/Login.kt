package com.example.cinevote.screens.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.PasswordInput
import com.example.cinevote.components.SimpleButton
import com.example.cinevote.components.TextInput
import com.example.cinevote.screens.auth.viewModel.AuthStatus
import com.example.cinevote.screens.auth.viewModel.AuthViewModel
import com.example.cinevote.screens.auth.viewModel.LoginActions
import com.example.cinevote.screens.auth.viewModel.LoginState

@Composable
fun LoginScreen(
    state: LoginState,
    actions: LoginActions,
    navController: NavHostController,
    auth:AuthViewModel
){

    Scaffold(
        containerColor= MaterialTheme.colorScheme.primaryContainer,
        //containerColor = colorResource(id = R.color.myRedTheatre),
        modifier= Modifier.background(Color.White)
    ) {innerPadding->
        Column(

            modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(
                modifier = Modifier
                    .padding(bottom = 40.dp, start = 20.dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.login_spot),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    //color = colorResource(id = R.color.myOrange) // Colore del testo aggiunto per maggior leggibilità
                    color= MaterialTheme.colorScheme.onPrimaryContainer
                )
            )


            Text(
                modifier = Modifier
                    .padding(bottom = 40.dp, start = 20.dp)
                    .fillMaxWidth(),
                text= stringResource(id = R.string.login_title),
                style= MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.Monospace,

            )

            TextInput("username", onChangeAction =actions::setUsername)
            Spacer(modifier = Modifier.padding(top= 20.dp))


            PasswordInput(onChangeAction =actions::setPassword)
            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ){
                SimpleButton(text = "Registrati", onClick = {navController.navigate(NavigationRoute.SignUpGeneral.route)}, modifier=Modifier.weight(1f), fontSize = 20.sp)

                SimpleButton(
                    text = "Accedi",
                    onClick = {

                        Log.d("test viewModel", state.username)

                        val correctKey = true; /*TODO*/
                        if(correctKey){
                            auth.changeState(AuthStatus.LOGGED)
                            navController.navigate(NavigationRoute.HomeScreen.route)

                            Log.d("loginstato", auth.state.value.status.toString())
                        } },
                    modifier=Modifier.weight(1f), fontSize = 20.sp)
            }

        }

    }
}






