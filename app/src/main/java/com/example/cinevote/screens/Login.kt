package com.example.cinevote.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinevote.R
import com.example.cinevote.components.FancyButton
import com.example.cinevote.components.PasswordInput
import com.example.cinevote.components.TextInput

@Composable
fun LoginForm(){
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
                modifier = Modifier.padding(bottom=40.dp, start=20.dp).fillMaxWidth(),
                text= stringResource(id = R.string.login_title),
                style= MaterialTheme.typography.headlineLarge,

            )

            TextInput("username")
            Spacer(modifier = Modifier.padding(top= 20.dp))
            PasswordInput()
            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ){
                FancyButton(text = "Registrati", onClick = {/*TODO*/}, modifier=Modifier.weight(1.1f))

                FancyButton(text = "Login", onClick = {/*TODO*/}, modifier=Modifier.weight(1f), fontSize = 23.sp)
            }

        }

    }
}




