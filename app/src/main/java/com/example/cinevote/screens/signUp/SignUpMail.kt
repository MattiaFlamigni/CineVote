package com.example.cinevote.screens.signUp

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.KeyBoard

import com.example.cinevote.components.SimpleButton
import com.example.cinevote.components.TextInput

@Composable
fun SignUpMailScreen(
    state: SignupState,
    actions: SignUPActions,
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier.background(Color.White),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) { innerPadding ->
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
                text = stringResource(id = R.string.registrati_title),
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.Monospace,

                )

            SignUpMailForm(navController, actions)

        }

    }
}


@Composable
private fun SignUpMailForm(navController: NavHostController, actions: SignUPActions) {


    var mail by remember { mutableStateOf("") }
    var mailError by remember { mutableStateOf(false) }

    var confirmMail by remember { mutableStateOf("") }
    var canEnable by remember { mutableStateOf(false) }


    mail = TextInput(role = "Mail", error = mailError, type = KeyBoard.MAIL, onChangeAction = {})
    confirmMail = TextInput(
        role = "Conferma Mail",
        error = mailError,
        type = KeyBoard.MAIL,
        onChangeAction = actions::setMail
    )



    if (confirmMail != mail && confirmMail.isNotEmpty()) {
        Text(text = "Le mail non corrispondono ")
        mailError = true
    } else if (!isValidEmailFormat(confirmMail) && confirmMail.isNotEmpty()) {
        mailError = true
        Text(text = "Mail non valida")
    } else {
        mailError = false
    }



    canEnable = !(mailError || mail.isEmpty() || confirmMail.isEmpty())







    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        SimpleButton(
            text = "Continua",
            onClick = { navController.navigate(NavigationRoute.SignUpPassword.route) },
            modifier = Modifier.weight(1.1f),
            fontSize = 20.sp,
            buttonEnabled = canEnable
        )

    }

}


private fun isValidEmailFormat(email: String): Boolean {
    // Implement your email validation logic here
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}






