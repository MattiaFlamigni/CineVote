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
import com.example.cinevote.components.KeyBoard

import com.example.cinevote.components.PasswordInput
import com.example.cinevote.components.SimpleButton
import com.example.cinevote.components.TextInput
import java.util.regex.Pattern

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

            SignUpForm(navController)

        }

    }
}


@Composable
fun SignUpForm(navController: NavHostController){

    var username by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") } ; var mailError by remember { mutableStateOf(false) }

    var confirmMail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } ; var passwordError by remember { mutableStateOf(false) }
    var canEnable by remember { mutableStateOf(false) }

    username=TextInput(role = "username")
    mail= TextInput(role = "Mail", error = mailError, type=KeyBoard.MAIL)
    confirmMail= TextInput(role = "Conferma Mail", error=mailError, type=KeyBoard.MAIL)
    password = PasswordInput(error = passwordError)
    confirmPassword = PasswordInput(error = passwordError)



    if(confirmMail!=mail && confirmMail.isNotEmpty()){
        Text(text = "Le mail non corrispondono ")
        mailError=true
    }else if(!isValidEmailFormat(confirmMail) && confirmMail.isNotEmpty()){
        mailError=true
        Text(text="Mail non valida")
    }else{
        mailError=false
    }




    if(confirmPassword!=password && password.isNotEmpty()){
        Text(text = "Le password non corrispondono")
        passwordError=true
    }else if(!isValidPassword(password) && confirmPassword.isNotEmpty()){
        Text(text = "Password non valida")
        passwordError=true
    }else{
        passwordError=false
    }

    if(passwordError || mailError || username.isEmpty()|| mail.isEmpty() || confirmMail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
        canEnable=false
    }else{
        canEnable=true
    }







    Row(
        modifier= Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ){
        SimpleButton(text = "Login", onClick = {navController.navigate(NavigationRoute.Login.route)}, modifier=Modifier.weight(1f), fontSize = 20.sp)

        SimpleButton(text = "Registrati", onClick = {/*TODO*/}, modifier=Modifier.weight(1.1f), fontSize = 20.sp, buttonEnabled = canEnable)

    }

}


private fun isValidEmailFormat(email: String): Boolean {
    // Implement your email validation logic here
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isValidPassword(password: String): Boolean {
    val passwordPattern = Pattern.compile(
        /* regex = */ "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@?!#\$%^&+=])(?=\\S+\$).{8,}"
    )
    return passwordPattern.matcher(password).matches()
}






