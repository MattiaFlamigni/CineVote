package com.example.cinevote.screens.auth

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

import com.example.cinevote.components.PasswordInput
import com.example.cinevote.components.SimpleButton
import java.util.regex.Pattern

@Composable
fun SignUpasswordScreen(navController:NavHostController){
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
                text = stringResource(id = R.string.signup_password),
                style = MaterialTheme.typography.headlineMedium,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight(50)
            )

            SignUpPasswordForm(navController)

        }

    }
}


@Composable
private fun SignUpPasswordForm(navController: NavHostController){

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } ; var passwordError by remember { mutableStateOf(false) }
    var canEnable by remember { mutableStateOf(false) }

    password = PasswordInput(error = passwordError)
    confirmPassword = PasswordInput(error = passwordError)





    if(confirmPassword!=password && password.isNotEmpty()){
        Text(text = "Le password non corrispondono")
        passwordError=true
    }else if(!isValidPassword(password) && confirmPassword.isNotEmpty()){
        Text(text = "Password non valida")
        passwordError=true
    }else{
        passwordError=false
    }

    if(passwordError || password.isEmpty() || confirmPassword.isEmpty()){
        canEnable=false
    }else{
        canEnable=true
    }

    Spacer(modifier = Modifier.padding(top=30.dp))
    val message = checkPasswordRequirements(password = password)
    Text(message)







    Row(
        modifier= Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ){

        SimpleButton(text = "Registrati", onClick = {/*TODO*/ navController.navigate(NavigationRoute.Login.route)}, modifier=Modifier.weight(1.1f), fontSize = 20.sp, buttonEnabled = canEnable)

    }

}


private fun isValidPassword(password: String): Boolean {
    val passwordPattern = Pattern.compile(
        /* regex = */ "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@?!#\$%^&+=])(?=\\S+\$).{8,}"
    )
    return passwordPattern.matcher(password).matches()
}




private fun checkPasswordRequirements(password: String): String {
    val lengthRequirement = if (password.length >= 8) "✔ Lunghezza minima di 8 caratteri" else "❌ Lunghezza minima di 8 caratteri"
    val uppercaseRequirement = if (password.any { it.isUpperCase() }) "✔ Contiene almeno una lettera maiuscola" else "❌ Deve contenere almeno una lettera maiuscola"
    val specialCharacterRequirement = if (password.any { it !in 'A'..'Z' && it !in 'a'..'z' && it !in '0'..'9' }) "✔️ Contiene almeno un carattere speciale" else "❌ Deve contenere almeno un carattere speciale"
    val digitRequirement = if (password.any { it.isDigit() }) "✔ Contiene almeno un numero" else "❌ Deve contenere almeno un numero"

    return buildString {
        appendLine("Requisiti della password:")
        appendLine(lengthRequirement)
        appendLine(uppercaseRequirement)
        appendLine(specialCharacterRequirement)
        append(digitRequirement)
    }
}
