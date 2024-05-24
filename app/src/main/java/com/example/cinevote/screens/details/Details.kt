package com.example.cinevote.screens.details


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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cinevote.components.TopBar
import com.example.cinevote.R


@Composable
fun DetailScreen(
    navController : NavHostController,
    state: DetailState,
    action: DetailAction,
    title:String
){
    var selectedChip by remember { mutableStateOf(ChipOption.TRAMA) }
    try {
        action.showDetails(title)
    }catch (e:Exception){
        Log.d("Inesistente", "Non presente nel db" )
        action.loadFromDb(title)
        action.showDetails(title)
    }


    Scaffold(
        topBar = { TopBar(navController = navController, title= stringResource(R.string.details_title)) },
        containerColor = MaterialTheme.colorScheme.background,

    ) {innerPadding->
        Surface(
            tonalElevation = 8.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxSize()
        ) {

            LazyColumn(
                modifier= Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
            ) {
                item {

                    Row(

                        horizontalArrangement = Arrangement.Center,

                        modifier = Modifier
                            .fillMaxWidth()
                            .width(200.dp),

                        ) {

                        GetImage(state.poster, state.title)



                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.padding(start = 20.dp, top = 40.dp)
                        ) {
                            IconToggle(
                                initialIcon = Icons.Default.FavoriteBorder,
                                finalIcon = Icons.Default.Favorite,
                                { /*TODO*/ },
                                {/*TODO*/ })
                            IconToggle(
                                initialIcon = Icons.Outlined.CheckCircle,
                                finalIcon = Icons.Default.CheckCircle,
                                {/*TODO*/ },
                                {/*TODO*/ })
                        }


                    }

                    Column(

                        modifier = Modifier
                            .fillMaxWidth()
                            .width(300.dp)
                            .padding(start = 25.dp)
                    ) {

                        Row(modifier = Modifier.fillMaxWidth()) {


                            for (genre in getGenres()) {
                                Text(
                                    text = genre,
                                    fontFamily = FontFamily.Default,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )

                                Text(
                                    text = "|",
                                    fontFamily = FontFamily.Default,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )


                            }
                        }
                        Spacer(modifier = Modifier.padding(top = 15.dp))
                        Text(
                            text = state.title,
                            fontFamily = FontFamily.Default,
                            fontSize = 40.sp,
                            lineHeight = 48.sp
                        )

                        Spacer(modifier = Modifier.padding(top = 15.dp))

                        Text(
                            text = "Anno: ${state.year}",
                            fontFamily = FontFamily.Default,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.padding(top = 15.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                modifier = Modifier.size(width = 100.dp, height = 50.dp),
                                colors = ButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = Color.Transparent
                                ),
                                onClick = { /*TODO*/ },
                                shape = RectangleShape

                                ) {
                                Text(text = "Valuta")
                            }
                        }


                        //ReviewRating(3) alternative:
                        GetVote(global = state.vote) /*TODO: LEGGERE VALUTAZIONE UTENTE*/


                        Spacer(modifier = Modifier.padding(top = 15.dp))
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        item {
                            AssistChipExample(
                                "trama",
                                Icons.Default.Info,
                                selectedChip == ChipOption.TRAMA
                            ) {
                                selectedChip = ChipOption.TRAMA
                            }

                        }
                        item {
                            AssistChipExample(
                                "Cast", /*TODO*/
                                Icons.Default.Person,
                                selectedChip == ChipOption.CAST
                            ) {
                                selectedChip = ChipOption.CAST
                            }

                        }
                        item {
                            AssistChipExample( /*TODO*/
                                "Recensioni",
                                Icons.Default.Star,
                                selectedChip == ChipOption.RECENSIONI
                            ) {
                                selectedChip = ChipOption.RECENSIONI
                            }
                        }
                    }


                    // Contenuto basato sul chip selezionato
                    when (selectedChip) {
                        ChipOption.TRAMA -> {
                            Text(
                                text = state.plot,
                                fontFamily = FontFamily.Serif,
                                fontSize = 24.sp,
                                modifier = Modifier
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                letterSpacing = 1.sp,
                                lineHeight = 30.sp,
                                textAlign = TextAlign.Justify
                            )
                        }

                        ChipOption.CAST -> {
                            Text("cast")
                        }

                        ChipOption.RECENSIONI -> {
                            Text("recensioni")
                        }



                    }

                    Spacer(modifier = Modifier.padding(bottom = 400.dp))
                }
            }

        }

    }
}



@Composable
private fun AssistChipExample(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    AssistChip(
        modifier = Modifier.padding(10.dp),
        onClick = { onClick() },
        label = { Text(text) },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = "",
                Modifier.size(AssistChipDefaults.IconSize),
            )
        },
        /*TODO: ADD COLORS*/
    )
}

@Composable
private fun IconToggle(initialIcon:ImageVector, finalIcon:ImageVector, firstClick:()->Unit, lastClick:()->Unit){
    var isAdded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { isAdded = !isAdded },
    ) {
        if (isAdded) {
            Icon(finalIcon, "info") // Icona visibile
            firstClick()
        } else {
            Icon(initialIcon, "info") // Icona nascosta
            lastClick()
        }
    }
}


@Composable
private fun GetImage(poster: String, title:String) {
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(
                LocalContext.current
            )
                .data(poster)
                .crossfade(true)
                .size(Size.ORIGINAL)
                .build()
        ),
        contentDescription = title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .size(300.dp, 300.dp)
    )
}

private fun getGenres(): List<String> {
    return listOf("Fantasy", "Avventura")
}


@Composable
private fun ReviewRating(index:Int) {


    Row {
        repeat(5) {current->
            Icon(
                imageVector = if (current < index) Icons.Sharp.Star else Icons.TwoTone.Star,
                contentDescription = null
            )
        }
    }
}


@Composable
private fun GetVote(global: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Valutazione globale
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Valutazione Globale",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$global/10",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // Valutazione utente
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Valutazione Utente",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "3/5",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }

}


