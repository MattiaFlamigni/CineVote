package com.example.cinevote.screens


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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.components.TopBar
import com.example.cinevote.R
import com.example.cinevote.components.bottomAppBar

@Composable
fun DetailScreen(navController : NavHostController){
    Scaffold(
        topBar = { TopBar(navController = navController, title= stringResource(R.string.details_title)) },
        bottomBar = { bottomAppBar(navController) },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,

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
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier=Modifier.padding(start=20.dp, top=40.dp)
                ) {
                    IconButton(onClick = {/*TODO*/}) {
                        Icon(Icons.Default.Info, "info")
                    }
                    IconButton(onClick = {/*TODO*/}) {
                        Icon(Icons.Default.AddCircle, "info")
                    }
                    IconButton(onClick = {/*TODO*/}) {
                        Icon(Icons.Default.Star, "info")
                    }

                }


            }

            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
                    .padding(start = 25.dp)
            ) {
                Column(
                    modifier= Modifier
                        .width(300.dp)
                        .fillMaxWidth()
                ){
                    Row {


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
                    
                    ReviewRating(3)

                    Spacer(modifier = Modifier.padding(top=15.dp))

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
            modifier = Modifier
                .size(300.dp, 300.dp)
        )

}

private fun getGenres(): List<String> {
    return listOf("Fantasy", "Avventura")
}

private fun getYear(): Int{
    return 2011
}

private fun getTitle():String{
    return "Pirati dei Caraibi"
}

@Composable
private fun ReviewRating(index:Int) {


    Row {
        repeat(5) {current->
            Icon(
                imageVector = if (current < index) Icons.Sharp.Star else Icons.Filled.Face,
                contentDescription = null
            )
        }
    }
}


