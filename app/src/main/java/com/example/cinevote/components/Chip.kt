package com.example.cinevote.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import com.example.cinevote.NavigationRoute

@Composable
fun simpleChip(icon: ImageVector, alternative:String, navController: NavHostController){
    var isSelected by remember { mutableStateOf(false) }

    Chip(
        modifier= Modifier
            .size(100.dp, 50.dp)
            .fillMaxWidth(),
        colors = ChipDefaults.chipColors(
            contentColor = Color.Black,
            backgroundColor = if(isSelected){Color.Green}else{Color.Transparent}
        ),
        onClick = { isSelected = true
                    if(alternative == "Home") {
                        navController.navigate(NavigationRoute.HomeScreen.route)
                    }

                    if(alternative=="OutNow"){
                        navController.navigate((NavigationRoute.OutNow.route))
                    }

            navController.navigate(NavigationRoute.Search.route) /*TODO*/

                  },
        label = { Text("") },
        icon = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ){
                Icon(icon, alternative)
            }

        }
    )
}