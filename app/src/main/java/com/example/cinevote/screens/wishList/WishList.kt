package com.example.cinevote.screens.wishList

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cinevote.NavigationRoute
import com.example.cinevote.components.TopBar


@Composable
fun WishListScreen(
    navController: NavHostController,
    state: wishListState,
    action: WishListAction
) {


    LaunchedEffect(key1 = true) {
        action.loadFilm()
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                navController = navController,
                title = "WishList"
            )
        },
        content = { innerPadding ->
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier.padding(innerPadding),
            ) {
                items(state.favoriteFilmList) { film ->
                    Card(
                        modifier = Modifier.padding(10.dp).width(50.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        onClick = { navController.navigate(NavigationRoute.Detail.buildRoute(film.title)) }
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(
                                    LocalContext.current
                                )
                                    .data("https://image.tmdb.org/t/p/w500" + film.posterPath)
                                    .size(Size.ORIGINAL)
                                    .build()
                            ),
                            contentDescription = film.title,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth()
                        )

                    }
                }


            }
        })
}


