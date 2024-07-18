package com.example.cinevote.screens.outNow

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.materialcore.Chip
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.TopBar
import com.example.cinevote.components.bottomAppBar


enum class TypeFilm{
    AL_CINEMA,
    FUTURO
}


@Composable
fun OutNowScreen(
    navController: NavHostController,
    state: OutNowStatus,
    action: FilmAction
) {

    val filmViewModel: OutNowVM = viewModel()
    var selectedFilter by filmViewModel.selectedFilter



    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.outNow_title)
            )
        },
        bottomBar = { bottomAppBar(navController) },
        content = { innerPadding ->


                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                ) {
                    AssistChip(
                        leadingIcon = {
                                      if(selectedFilter==TypeFilm.AL_CINEMA){
                                          Icon(Icons.Default.Check, "check")
                                      }
                        },
                        onClick = { selectedFilter = TypeFilm.AL_CINEMA },
                        label = { Text(text = "Al cinema") },

                    )
                    AssistChip(
                        leadingIcon = {
                            if(selectedFilter==TypeFilm.FUTURO){
                                Icon(Icons.Default.Check, "check")
                            }
                        },
                        onClick = { selectedFilter = TypeFilm.FUTURO },
                        label = { Text(text = "In futuro") }
                    )
                }




            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(top=100.dp),
            ) {


                if(selectedFilter==TypeFilm.AL_CINEMA) {
                    action.getFilmList()
                }else{
                    action.getFutureFilm()
                }

                Log.d("film", state.filmList.size.toString())

                for (film in state.filmList) {
                    Log.d("path", film.title)
                    Log.d("path", film.releaseDate)
                    Log.d("path", film.posterUrl)
                    item {
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
                                painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(film.posterUrl)
                                        .size(coil.size.Size.ORIGINAL)
                                        .build()
                                ),
                                contentDescription = film.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(300.dp)
                            )

                        }
                    }
                }

            }
        })
}


