package com.example.cinevote.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.components.TopBar
import com.example.cinevote.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

@Composable
fun SettingsScreen(
    navController : NavHostController,
    action: SettingsAction
){

    Scaffold(

        topBar = { TopBar(title="Impostazioni", navController=navController) }
    ) {innerPadding->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            ShowProfile(action)
        }
    }
}


@Composable
private fun ShowProfile(action: SettingsAction){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id =R.drawable.ic_launcher_foreground),
            contentDescription = "profile image",
            modifier = Modifier.size(200.dp)

        )



        Text(
            text = action.setName(),
            fontFamily = FontFamily.Default,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(onClick = { /*TODO*/ }) {
            Text("Edit profile")
        }
    }
}

@Composable
private fun ShowOption(){

}

