package com.example.cinevote.screens.outNow

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.FilmCard
import com.example.cinevote.components.TopBar
import com.example.cinevote.components.bottomAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


@Composable
fun OutNowScreen(
    navController: NavHostController,
    state :OutNowStatus,
    action: FilmAction
){






    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopBar(
            navController = navController,
            title= stringResource(R.string.outNow_title)
        )},
        bottomBar = { bottomAppBar(navController) },
        content = {innerPadding->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 200.dp),
            modifier=Modifier.padding(innerPadding),
        ){

            action.getFilmList()

            Log.d("film", state.filmList.size.toString())

            for(film in state.filmList){
                Log.d("path", film.title)
                Log.d("path", film.releaseDate)
                Log.d("path", film.posterUrl)
                item(){
                Card(
                    modifier= Modifier.padding(10.dp),
                    colors= CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    onClick = {navController.navigate(NavigationRoute.Detail.buildRoute(film.title))}
                ){
                    Image(
                        painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
                            .data(film.posterUrl)
                            .size(coil.size.Size.ORIGINAL)
                            .build()),
                        contentDescription = film.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )

                }}
            }

        }
    })
}


