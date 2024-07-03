package com.example.cinevote.util

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import com.example.cinevote.R

@Composable
fun currentRoute(navController:NavHostController): String? =
    navController.currentBackStackEntryAsState().value?.destination?.route



@Composable
fun LoadThumbnail(
    mediaPath: String,
    isVideo: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    if (isVideo) {
        val imageLoader = remember {
            ImageLoader.Builder(context)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .respectCacheHeaders(true)
                .components {

                }
                .crossfade(true)
                .build()
        }
        val painter = rememberAsyncImagePainter(
            model = mediaPath,
            imageLoader = imageLoader,
        )

        if (painter.state is AsyncImagePainter.State.Loading) {
            Image(
                painter = painterResource(id = R.drawable.outlined_star),
                contentDescription = null,
                modifier = modifier,
                contentScale = ContentScale.Crop,
            )
        }

        Image(
            painter = painter,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        AsyncImage(
            model = mediaPath,
            contentDescription = "",
            modifier = modifier,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            error = painterResource(id = R.drawable.star)
        )
    }
}