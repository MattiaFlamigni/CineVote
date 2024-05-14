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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute
import com.example.cinevote.components.TopBar
import com.example.cinevote.R

@Composable
fun SettingsScreen(
    navController : NavHostController,
    state:SettingsStatus,
    action: SettingsAction
){

    Scaffold(

        topBar = { TopBar(title="Impostazioni", navController=navController) }
    ) {innerPadding->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            ShowProfile(state)
            ShowOption(action=action, navController=navController)
        }
    }
}


@Composable
private fun ShowProfile(state: SettingsStatus){
    var username by remember { mutableStateOf("") }



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
            text =state.username,
            fontFamily = FontFamily.Default,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = state.name,
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
private fun ShowOption(action: SettingsAction, navController: NavHostController){
    val options = listOf(SettingItem.IMPOSTAZIONI_TEMA, SettingItem.LOGOUT)

    Column(
        modifier = Modifier.fillMaxWidth().padding(top=20.dp),
        //horizontalArrangement = Arrangement.Center
    ){

        for(option in options){

            TextButton(
                onClick = {
                    when(option){
                        SettingItem.LOGOUT->{
                            action.logOut()
                            navController.navigate(NavigationRoute.Login.route)
                        }

                        SettingItem.IMPOSTAZIONI_TEMA -> TODO()
                    }


                },
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text=option.label,
                    fontFamily = FontFamily.Default,
                    fontSize = 30.sp
                )
            }

        }
    }
}


