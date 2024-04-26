package com.example.cinevote.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinevote.R


@Composable
fun FilmCard(category:String=""){

    Column {

        Text(
            text=category,
            fontFamily = FontFamily.Monospace,
            fontSize = 30.sp
        )


        LazyRow {
            items(50) {
                Card(
                    modifier= Modifier.padding(10.dp),
                    colors= CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ){
                    val image: Painter = painterResource(id = R.drawable.ic_launcher_foreground)
                    Image(painter = image, contentDescription = "")

                }
            }
        }
    }

}