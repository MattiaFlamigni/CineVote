    package com.example.cinevote.screens.search


import android.graphics.PathIterator.Segment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.TopBar
import com.example.cinevote.components.bottomAppBar
import kotlinx.coroutines.launch

    @Composable
fun searchScreen(navController:NavHostController, state : SearchStatus, action: searchAction) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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
            Search(navController, state, action)
        }
    }
}




    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Search(navController: NavHostController, state : SearchStatus, action: searchAction){
        var text by remember { mutableStateOf("") } // Query for SearchBar
        var active by remember { mutableStateOf(false) } // Active state for SearchBar
        val coroutineScope = rememberCoroutineScope()

        SearchBar(modifier = Modifier.fillMaxWidth(),
            query = text,
            onQueryChange = {
                text = it
                coroutineScope.launch {
                    action.searchFilmByTitle(text)
                }
            },
            onSearch = {
                active = true
                Log.d("OnSearch", "searching")

                coroutineScope.launch {
                    action.searchFilmByTitle(text)
                }



            },
            active = active,
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(text = "Cerca Film")
            },

            trailingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }) {


            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(state.listfilm){film->

                    Card(
                        modifier= Modifier.padding(10.dp),
                        colors= CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        onClick = {navController.navigate(NavigationRoute.Detail.buildRoute(film.title))}
                    ){
                        Image(
                            painter = rememberAsyncImagePainter(model = ImageRequest.Builder(
                                LocalContext.current)
                                .data(film.posterUrl)
                                .size(Size.ORIGINAL)
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
