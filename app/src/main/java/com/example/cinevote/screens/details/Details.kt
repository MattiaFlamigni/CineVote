package com.example.cinevote.screens.details


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.components.TopBar
import com.example.cinevote.R
import com.example.cinevote.components.bottomAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController : NavHostController){
    var selectedChip by remember { mutableStateOf(ChipOption.TRAMA) }

    Scaffold(
        topBar = { TopBar(navController = navController, title= stringResource(R.string.details_title)) },
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
                    IconToggle(initialIcon = Icons.Default.FavoriteBorder, finalIcon = Icons.Default.Favorite, { /*TODO*/},{/*TODO*/}  )
                    IconToggle(initialIcon = Icons.Outlined.CheckCircle, finalIcon = Icons.Default.CheckCircle, {/*TODO*/}, {/*TODO*/} )
                    IconButton(onClick = {/*TODO*/}) {
                        Icon(Icons.Default.Star, "info")
                    }

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

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                item {
                    AssistChipExample("trama", Icons.Default.Info, selectedChip==ChipOption.TRAMA){
                        selectedChip = ChipOption.TRAMA
                    }

                }
                item{
                    AssistChipExample("Cast", Icons.Default.Person, selectedChip==ChipOption.CAST){
                        selectedChip = ChipOption.CAST
                    }

                }
                item{
                    AssistChipExample("Recensioni", Icons.Default.Star, selectedChip==ChipOption.RECENSIONI){
                        selectedChip = ChipOption.RECENSIONI
                    }
                }
            }


            // Contenuto basato sul chip selezionato
            when(selectedChip) {
                ChipOption.TRAMA -> {
                    Text("trama")
                }
                ChipOption.CAST -> {
                    Text("cast")
                }
                ChipOption.RECENSIONI -> {
                    Text("recensioni")
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
                imageVector = if (current < index) Icons.Sharp.Star else Icons.Rounded.Face,
                contentDescription = null
            )
        }
    }
}


