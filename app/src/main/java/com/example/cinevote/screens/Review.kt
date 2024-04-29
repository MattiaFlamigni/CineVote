package com.example.cinevote.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.components.TopBar
import com.example.cinevote.R
import com.example.cinevote.components.bottomAppBar

@Composable
fun ReviewScreen(navController : NavHostController){
    Scaffold(
        topBar = { TopBar(navController = navController) },
        bottomBar = { bottomAppBar(navController) },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        floatingActionButton = { floatingActButton() }
    ) {innerPadding->




        Column(
            modifier= Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
        ) {

            Row(

                horizontalArrangement = Arrangement.Center,

                modifier = Modifier
                    .fillMaxWidth()
                    .width(200.dp),

                ){

                GetImage()

            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
            ) {
                Column(
                    modifier= Modifier
                        .width(300.dp)
                        .fillMaxWidth()
                ){
                    Row() {


                        for (genre in getGenres()) {
                            Text(
                                text = genre,
                                fontFamily = FontFamily.Default,
                                fontSize = 20.sp,
                                color=MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Text(
                                text = "|",
                                fontFamily = FontFamily.Default,
                                fontSize = 20.sp,
                                color=MaterialTheme.colorScheme.onPrimaryContainer
                            )


                        }
                    }
                    Spacer(modifier = Modifier.padding(top=15.dp))
                    Text(
                        text = getTitle(),
                        fontFamily = FontFamily.Default,
                        fontSize = 40.sp,
                        lineHeight = 48.sp
                    )

                    Spacer(modifier = Modifier.padding(top=15.dp))

                    Text(
                        text = getYear().toString(),
                        fontFamily = FontFamily.Default,
                        fontSize = 20.sp,
                        color=MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.padding(top=15.dp))
                    
                    ReviewRating()

                    Spacer(modifier = Modifier.padding(top=15.dp))

                    DescriptionTextField()


                }

            }







        }
    }
}



@Composable
private fun GetImage(){


        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "image",
            modifier = Modifier.size(300.dp, 300.dp)
        )

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
private fun ReviewRating() {
    var selectedStars by remember { mutableStateOf(0) }

    Row {
        repeat(5) { index ->
            IconButton(
                onClick = { selectedStars = index + 1 },
                modifier = Modifier.padding(4.dp)
            ) {
                Icon(
                    imageVector = if (index < selectedStars) Icons.Sharp.Star else Icons.Filled.Face,
                    contentDescription = null
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DescriptionTextField() {
    var description by remember { mutableStateOf("") }

   val containerColor = MaterialTheme.colorScheme.secondaryContainer

    TextField(
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
        ),
        value = description,
        onValueChange = { description = it },
        placeholder = { Text("Inserisci la descrizione qui") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        singleLine = false, // Per consentire più righe di testo
        maxLines = 5 // Limite massimo di righe di testo
    )
}


@Composable
private fun floatingActButton(){
    FloatingActionButton(
        onClick = { /*TODO*/ },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Icon(Icons.Default.Done, "Add")

    }
}


