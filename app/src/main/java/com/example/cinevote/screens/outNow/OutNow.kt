package com.example.cinevote.screens.outNow

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.FilmCard
import com.example.cinevote.components.TopBar
import com.example.cinevote.components.bottomAppBar


@Composable
fun OutNowScreen(navController: NavHostController){
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopBar(
            navController = navController,
            title= stringResource(R.string.outNow_title)
        )},
        bottomBar = { bottomAppBar(navController) },
        content = {innerPadding->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            modifier=Modifier.padding(innerPadding),
        ){

            items(LoadWishList().size) {
                FilmCard(onClick = {navController.navigate(NavigationRoute.Detail.route)})
            }

        }
    })
}


private fun LoadWishList(): List<String> {
    return listOf("uno", "due", "tre", "quattro")
}


