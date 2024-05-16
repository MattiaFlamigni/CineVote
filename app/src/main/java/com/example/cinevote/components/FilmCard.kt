package com.example.cinevote.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.cinevote.R
import com.example.cinevote.screens.outNow.Film


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmCard(film: Film, category: String = "", onClick:()->Unit, image:Painter = painterResource(id = R.drawable.ic_launcher_foreground)){

    Column {

        Text(
            text=category,
            fontFamily = FontFamily.Monospace,
            fontSize = 30.sp
        )


        LazyRow {
            items(1) {
                Card(
                    modifier= Modifier.padding(10.dp),
                    colors= CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    onClick = onClick
                ){
                    Image(
                        painter = rememberAsyncImagePainter(model = film.posterUrl),
                        contentDescription = film.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )

                }
            }
        }
    }

}