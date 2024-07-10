package com.example.cinevote.screens.review

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cinevote.components.TopBar
import com.example.cinevote.R
import com.example.cinevote.components.bottomAppBar
import com.example.cinevote.screens.details.DetailAction
import com.example.cinevote.screens.details.DetailState

@Composable
fun ReviewScreen(navController : NavHostController, title:String, state: ReviewState,
                 action: ReviewAction,){
    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = { bottomAppBar(navController) },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        floatingActionButton = { floatingActButton(action, title ) }
    ) {innerPadding->


        action.setYear(title)



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

                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(state.image)
                                .size(Size.ORIGINAL)
                                .build()
                        ),
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )

                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(300.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .width(300.dp)
                            .fillMaxWidth()
                    ) {
                        Row() {


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
                            text = title,
                            fontFamily = FontFamily.Default,
                            fontSize = 40.sp,
                            lineHeight = 48.sp
                        )

                        Spacer(modifier = Modifier.padding(top = 15.dp))

                        Text(
                            text = state.year.toString(),
                            fontFamily = FontFamily.Default,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.padding(top = 15.dp))

                        ReviewRating(state, action)

                        Spacer(modifier = Modifier.padding(top = 15.dp))

                        DescriptionTextField(action)


                    }

                }
            }

        }
    }
}

private fun getGenres(): List<String> {
    return listOf<String>("Fantasy", "Avventura")
}

private fun getYear(): Int{
    return 2011
}

private fun getTitle():String{
    return "Pirati dei Caraibi"
}

@Composable
private fun ReviewRating(state: ReviewState, action: ReviewAction) {
    var selectedStars by remember { mutableStateOf(0) }

    Row {
        repeat(5) { index ->
            IconButton(
                onClick = {
                    selectedStars = index + 1
                    action.setRating(selectedStars)
                },
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    painter = if (index < selectedStars) painterResource(id = R.drawable.star) else painterResource(id= R.drawable.outlined_star),
                    contentDescription = null
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DescriptionTextField(action:ReviewAction) {
    var description by remember { mutableStateOf("") }


   val containerColor = MaterialTheme.colorScheme.secondaryContainer

    TextField(
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
        ),
        value = description,
        onValueChange = {
            description = it
            action.setDesc(it)
        },
        placeholder = { Text("Inserisci la descrizione qui") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        singleLine = false, // Per consentire più righe di testo
        maxLines = 5 // Limite massimo di righe di testo
    )
}


@Composable
private fun floatingActButton(action: ReviewAction, title:String){
    FloatingActionButton(
        onClick = { action.uploadReview(title) },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Icon(Icons.Default.Done, "Add")

    }
}


