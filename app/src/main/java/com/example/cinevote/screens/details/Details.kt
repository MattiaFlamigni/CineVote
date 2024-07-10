package com.example.cinevote.screens.details


import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.TopBar
import com.example.cinevote.data.Review


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    state: DetailState,
    action: DetailAction,
    title: String
) {
    var selectedChip by remember { mutableStateOf(ChipOption.TRAMA) }
    try {
        action.showDetails(title)
        action.hasReview(title)
        action.isFavorite(title)
        action.loadReview(title)

    } catch (e: Exception) {
        Log.d("Inesistente", "Non presente nel db")
        action.loadFromDb(title)
        action.showDetails(title)
    }


    Scaffold(
        topBar = {
            /*TopBar(
                navController = navController,
                title = stringResource(R.string.details_title)
            )*/




            CenterAlignedTopAppBar(
                title={ Text(
                    text=title,
                    fontFamily = FontFamily.Default,
                    fontSize = 30.sp,
                    letterSpacing = 0.30.sp,
                    color= Color.Black

                ) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = Color.Black
                ),

                navigationIcon = {


                    if(title!= stringResource(id = R.string.home_title)) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Default.ArrowBack, "back")
                        }
                    }
                },
                actions = {


                    if(state.isFavorite){
                        IconButton(onClick = { action.addToWishList(title) }) {
                            Icon(painterResource(id = R.drawable.star), "star")
                        }
                    }else{
                        IconButton(onClick = { action.addToWishList(title) }) {
                            Icon(painterResource(id = R.drawable.outlined_star), "star")
                        }
                    }





                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background,

        ) { innerPadding ->
        Surface(
            tonalElevation = 8.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier
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
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                enabled = state.isReviewd,
                                modifier = Modifier.size(width = 150.dp, height = 50.dp),
                                colors = ButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContainerColor = Color.Gray,
                                    disabledContentColor = Color.White
                                ),
                                onClick = {
                                            navController.navigate(NavigationRoute.Review.buildRoute(state.title))

                                          },
                                shape = RectangleShape

                            ) {
                                if(state.isReviewd){
                                    Text(text = "Valuta")
                                }else {
                                    Text("Valutato")
                                }
                            }


                            val ctx = LocalContext.current

                            Button(
                                modifier = Modifier.size(width = 150.dp, height = 50.dp),
                                colors = ButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContainerColor = Color.Transparent,
                                    disabledContentColor = Color.Transparent
                                ),

                                onClick = {

                                    action.getIdFromTitle(title) { id ->

                                        action.getKeyTrailer(id) { key ->
                                            if (key.isNotEmpty()) {
                                                val videoUrl =
                                                    "https://www.youtube.com/watch?v=$key"
                                                Log.d("URIYOUTUBE", videoUrl)

                                                val intent =
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                ctx.startActivity(intent)
                                            }

                                        }


                                    }
                                },
                                shape = RectangleShape

                            ) {
                                Text(text = "Trailer")
                            }
                        }


                        //ReviewRating(3) alternative:
                        GetVote(global = state.vote, state)


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
                                "Cast",
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
                            /*Text(
                                text = state.plot,
                                fontFamily = FontFamily.Serif,
                                fontSize = 24.sp,
                                modifier = Modifier
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                letterSpacing = 1.sp,
                                lineHeight = 30.sp,
                                textAlign = TextAlign.Justify
                            )*/

                            PlotSection(plot =state.plot)
                        }

                        ChipOption.CAST -> {
                            val tmdbBaseUrl = "https://image.tmdb.org/t/p/w500"
                            action.getIdFromTitle(title) { id ->
                                action.loadActor(id)
                            }


                            // Dividi gli attori in gruppi di tre per riga
                            val actorsInRows = state.actorListState.chunked(3)

                            // Itera sui gruppi di attori
                            actorsInRows.forEach { rowOfActors ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    // Itera sugli attori nella riga corrente
                                    rowOfActors.forEach { actor ->
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(
                                                painter = rememberAsyncImagePainter(model = "$tmdbBaseUrl${actor.profilePic}"),
                                                contentDescription = actor.name,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.size(100.dp)
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = actor.name,
                                                style = MaterialTheme.typography.labelLarge,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1
                                            )
                                            Text(
                                                text = actor.job,
                                                style = MaterialTheme.typography.labelLarge,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                                // Aggiungi uno spazio tra le righe di attori
                                Spacer(modifier = Modifier.height(8.dp))
                            }


                        }

                        ChipOption.RECENSIONI -> {
                            //Text("recensioni"
                            for(review in state.reviewList){
                                ReviewCard(review = review)
                            }



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
private fun IconToggle(
    initialIcon: ImageVector,
    finalIcon: ImageVector,
    firstClick: () -> Unit,
    lastClick: () -> Unit
) {
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
private fun GetImage(poster: String, title: String) {
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
private fun ReviewRating(index: Int) {


    Row {
        repeat(5) { current ->
            Icon(
                imageVector = if (current < index) Icons.Sharp.Star else Icons.TwoTone.Star,
                contentDescription = null
            )
        }
    }
}


@Composable
private fun GetVote(global: Float, state: DetailState) {
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
                    text = if (state.userVote > 0) {
                        "${state.userVote.toString()}/5"
                    } else {
                        "-"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }

}



@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = review.descrizione,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingBar(rating = review.stelle)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Autore: ${review.autore}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun RatingBar(rating: Int) {
    Row {
        repeat(rating) {
            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = "Filled star",
                tint = Color.Yellow
            )
        }
        repeat(5 - rating) {
            Icon(
                painter = painterResource(id = R.drawable.outlined_star),
                contentDescription = "Empty star",
                tint = Color.Gray
            )
        }
    }
}




@Composable
fun PlotSection(plot: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_background), // Assicurati di avere un'icona adeguata
                    contentDescription = "Plot Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Trama",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = plot,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}



