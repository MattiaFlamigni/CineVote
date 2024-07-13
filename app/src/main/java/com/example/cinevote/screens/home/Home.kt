package com.example.cinevote.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.TopBar
import com.example.cinevote.components.bottomAppBar
import com.example.cinevote.data.Genre

@Composable
fun HomeScreen(
    navController: NavHostController,
    actions: HomeAction,
    state: HomeState,
) {
    Scaffold(
        bottomBar = { bottomAppBar(navController) },
        topBar = {
            TopBar(
                title = stringResource(id = R.string.home_title),
                navController = navController
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 10.dp)
            ) {
                item {
                    TopCategory(navController, state, actions)




                    LaunchedEffect(Unit) {
                        actions.getFilmsByGenre(1, 28)
                        actions.loadFilm()
                    }





                    for (genre in Genre.entries) {


                        TextButton(
                            onClick = {
                                navController.navigate(
                                    NavigationRoute.Expand.buildRoute(genre.id)
                                )
                            }


                            /*onClick = { navController.navigate(NavigationRoute.Expand.buildRoute(genre.id))

                            Log.d("route",
                                navController.navigate(NavigationRoute.Expand.buildRoute(genre.id))
                                    .toString())
                        }*/
                        ) {
                            Text(
                                text = genre.name,
                            )
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "expand")

                        }


                        LazyRow {

                            val films = state.filmsByGenre


                            items(films.size / 4) { index ->

                                val genres = films[index].genreIDs.split(",")
                                    .mapNotNull { it.trim().toIntOrNull() }


                                if (genres.contains(genre.id)) {


                                    val film = films[index]
                                    Card(
                                        modifier = Modifier.padding(10.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                                        ),
                                        onClick = {
                                            navController.navigate(
                                                NavigationRoute.Detail.buildRoute(
                                                    film.title
                                                )
                                            )
                                        }
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(model = film.posterPath),
                                            contentDescription = film.title,
                                            //contentDescription = "",
                                            //painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .size(100.dp)
                                        )

                                    }
                                }
                            }


                        }

                    }


                }
            }
        }
    }
}

@Composable
private fun TopCategory(navController: NavHostController, state: HomeState, actions: HomeAction) {

    LaunchedEffect(Unit) {
        actions.getTop()
    }

    Column {
        Text(
            text = "Top Film",
            fontFamily = FontFamily.Default,
            fontSize = 50.sp
        )


        LazyRow {

            for (film in state.topFilmList) {

                item {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(200.dp),
                        onClick = { navController.navigate(NavigationRoute.Detail.buildRoute(film.title)) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(film.posterUrl)
                                    .size(coil.size.Size.ORIGINAL)
                                    .build()
                            ),
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


