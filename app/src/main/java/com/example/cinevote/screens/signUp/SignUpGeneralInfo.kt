package com.example.cinevote.screens.signUp


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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

import com.example.cinevote.components.SimpleButton
import com.example.cinevote.components.TextInput

@Composable
fun SignUpGeneralScreen(
    state: SignupState,
    actions : SignUPActions,
    navController:NavHostController
){
    Scaffold(
        modifier= Modifier.background(Color.White),
        containerColor = MaterialTheme.colorScheme.primaryContainer
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
                text = stringResource(id =R.string.registration_spot),
                style = MaterialTheme.typography.headlineMedium,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight(50)


            )

            Text(
                modifier = Modifier
                    .padding(bottom = 40.dp, start = 20.dp)
                    .fillMaxWidth(),
                text= stringResource(id = R.string.registrati_title),
                style= MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.Monospace,
            )
            SignUpGeneralForm(navController, actions, state)

        }

    }
}


@Composable
private fun SignUpGeneralForm(navController: NavHostController, actions: SignUPActions, state :SignupState){

    var username by remember { mutableStateOf("") }

    var nome by remember { mutableStateOf("") }
    var cognome by remember { mutableStateOf("") }

    var canEnable by remember { mutableStateOf(false) }
    var userError by remember { mutableStateOf(false) }

    nome= TextInput(role = "Nome", onChangeAction = actions::setName)
    cognome = TextInput(role = "Cognome", onChangeAction = actions::setSurname)
    username=TextInput(role = "username", onChangeAction = actions::setUsername, error=userError)
    
    Spacer(modifier = Modifier.padding(20.dp))
    if(userError) {
        Text(text = stringResource(id = R.string.user_error_strings))
    }


    canEnable = !(username.isEmpty() || nome.isEmpty() || cognome.isEmpty())

    Row(
        modifier= Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ){
        SimpleButton(text = "Login", onClick = {navController.navigate(NavigationRoute.Login.route)}, modifier=Modifier.weight(1f), fontSize = 20.sp)

        SimpleButton(text = "Continua", onClick = {

            isUsernameAvailable(username) { isAvailable ->
                if (isAvailable) {
                    navController.navigate((NavigationRoute.SignUpMail.route))
                    userError=false
                } else {
                    // Username is not available, show an error message
                    userError=true
                }
            }


        }, modifier=Modifier.weight(1.1f), fontSize = 20.sp, buttonEnabled = canEnable)

    }

}










