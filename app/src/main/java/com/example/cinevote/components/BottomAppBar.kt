package com.example.cinevote.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute


@Composable
fun bottomAppBar(navController: NavHostController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = Color.Black,
        modifier=Modifier.fillMaxWidth(),
        tonalElevation = 100.dp,
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly

            ){
                simpleChip(Icons.Default.Home, "Home",navController)
                simpleChip(Icons.Default.Search, "search",navController)
                simpleChip(Icons.Default.PlayArrow, "in the cinema", navController)
                simpleChip(Icons.Default.Face, "seen", navController)

            }


        }
    )
}





