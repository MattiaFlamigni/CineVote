package com.example.cinevote.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.cinevote.R
import com.example.cinevote.components.Search
import com.example.cinevote.components.TopBar
import com.example.cinevote.components.bottomAppBar

@Composable
fun searchScreen(navController:NavHostController) {
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.search_title)
            )
        },
        bottomBar = { bottomAppBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Search()
        }
    }
}