package com.example.cinevote.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.TopBar
import com.example.cinevote.components.bottomAppBar
import com.example.cinevote.data.Film
import com.example.cinevote.screens.outNow.OutNowStatus


@Composable
fun HomeScreen(
    navController: NavHostController,
    actions : HomeAction,
    state : HomeState
){
    for(i in 0..3){
        actions.getAll(i)
    }



    Scaffold(
        bottomBar = { bottomAppBar(navController) },
        topBar = { TopBar(title= stringResource(id = R.string.home_title),navController= navController)},
        containerColor = MaterialTheme.colorScheme.background
    ) {innerPadding->
        Surface(

        ){
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 10.dp)
            ) {
                item(){
                    TopCategory(navController, state, actions)
                    }

                    item {
                        for (genre in loadGenres()) {
                            Text(
                                text = genre,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 30.sp
                            )


                            Card(
                                modifier = Modifier.padding(10.dp).size(100.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                                ),
                                onClick = { /*TODO*/ }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxWidth()
                                )

                            }
                        }
                    }

            }

        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopCategory(navController: NavHostController, state: HomeState, actions: HomeAction) {

    actions.getTop()

        Column {
            Text(
                text = "Top Film",
                fontFamily = FontFamily.Default,
                fontSize = 50.sp
            )


            LazyRow {

                for (film in state.topFilmList) {

                    item() {
                        Card(
                            modifier = Modifier
                                .padding(10.dp)
                                .size(200.dp),
                            onClick = { navController.navigate(NavigationRoute.Detail.route) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
                                    .data(film.posterUrl)
                                    .size(coil.size.Size.ORIGINAL)
                                    .build()),
                                contentDescription = film.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

            }
        }
}





fun loadGenres(): List<String> {
    return listOf("Commedia", "Horror", "Comico", "Thriller", "Prova")
}
