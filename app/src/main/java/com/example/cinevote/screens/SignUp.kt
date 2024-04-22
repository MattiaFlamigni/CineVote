package com.example.cinevote.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.FancyButton

import com.example.cinevote.components.PasswordInput
import com.example.cinevote.components.SimpleButton
import com.example.cinevote.components.TextInput

@Composable
fun SignUpScreen(navController:NavHostController){
    Scaffold(
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
                text= stringResource(id = R.string.registrati_title),
                style= MaterialTheme.typography.headlineLarge,

            )

            SignUpForm()

            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ){
                SimpleButton(text = "Login", onClick = {navController.navigate(NavigationRoute.Login.route)}, modifier=Modifier.weight(1f), fontSize = 20.sp)

                SimpleButton(text = "Registrati", onClick = {/*TODO*/}, modifier=Modifier.weight(1.1f), fontSize = 20.sp)

            }

        }

    }
}


@Composable
fun SignUpForm(){

    var username by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var confirmMail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }


    username=TextInput(role = "username")
    mail= TextInput(role = "Mail")
    confirmMail= TextInput(role = "Conferma Mail")
    password = PasswordInput()
    confirmPassword = PasswordInput()



    if(confirmMail!=mail){
        Text(text = "Le mail non corrispondono ")
    }

    if(confirmPassword!=password){
        Text(text = "Le Password non corrispondono ")
    }



}



