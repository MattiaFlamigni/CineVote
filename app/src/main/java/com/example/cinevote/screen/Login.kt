package com.example.cinevote.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinevote.components.FancyButton
import com.example.cinevote.components.SimpleButton

@Composable
fun Login(){
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
                modifier = Modifier.padding(bottom=40.dp),
                text="Login into CineVote",
                style= MaterialTheme.typography.headlineLarge
            )

            UsernameInput()
            Spacer(modifier = Modifier.padding(top= 20.dp))
            PasswordInput()
            Row(
                modifier=Modifier.fillMaxWidth().padding(top=20.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ){
                FancyButton(text = "Registrati", onClick = {/*TODO*/}, modifier=Modifier.weight(1.1f))

                FancyButton(text = "Login", onClick = {/*TODO*/}, modifier=Modifier.weight(1f), fontSize = 23.sp)
            }

        }

    }
}



@Composable
fun UsernameInput() {
    var username by remember { mutableStateOf("") }

    OutlinedTextField(
        value = username,
        onValueChange = { username = it },
        label = { Text("Username") }
    )
}


@Composable
fun PasswordInput() {
    var password by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    )
}
