package com.example.cinevote.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.R
import com.example.cinevote.components.FilmCard
import com.example.cinevote.components.TopBar



@Composable
fun HomeScreen(navController: NavHostController){
    Scaffold(
        topBar = { TopBar(title= stringResource(id = R.string.home_title),navController= navController)},
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {innerPadding->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 10.dp)
        ) {
            item {
                TopCategory()


                for(genre in loadGenres()) {
                    FilmCard(genre)
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopCategory() {



    Column {
        Text(
            text="Top Film",
            fontFamily = FontFamily.Default,
            fontSize = 50.sp
        )


        LazyRow {
            items(50) {
                Card(
                    modifier= Modifier
                        .padding(10.dp)
                        .size(200.dp),
                    onClick = {/*TODO*/},
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





fun loadGenres(): List<String> {
    return listOf("Commedia", "Horror", "Comico", "Thriller", "Prova")
}