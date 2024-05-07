package com.example.cinevote.screens.cinema


import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.components.TopBar


@Composable
fun CinemaScreen(
    navController : NavHostController,
    state: CinemaStatus,
    actions: ()->Unit
) {
    Scaffold(
        topBar = { TopBar(navController = navController, title = "Cinema nelle vicinanze") },
        modifier = Modifier.fillMaxWidth()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                val ctx = LocalContext.current

                actions.invoke()

                if (state.cinemaList.isEmpty()) {
                    Text(
                        text = "Nessun cinema trovato nelle vicinanze",
                        modifier = Modifier.padding(top = 20.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    // Loop through the list of cinemas and display them
                    state.cinemaList.forEach { cinema ->
                        OutlinedCardExample(cinema.name, cinema.address, cinema.city, onClick = {
                            val uri = Uri.parse("geo:${cinema.latitude},${cinema.longitude}?q=${Uri.encode(cinema.name+cinema.city)}")
                            val intent = Intent(Intent.ACTION_VIEW).apply { data=uri }
                            if(intent.resolveActivity(ctx.packageManager)!=null){
                                ctx.startActivity(intent)
                            }
                        })
                    }
                }

        }



        }
    }
}


@Composable
private fun OutlinedCardExample(name: String, address: String, city:String, onClick:()->Unit ) {

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        //backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .width(400.dp)
            .height(200.dp)
            .padding(20.dp),


    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = city,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = address,
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily.Default,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ViewCinema(){
    val ctx = LocalContext.current
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "geo/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share travel")
    if (shareIntent.resolveActivity(ctx.packageManager) != null) {
        ctx.startActivity(shareIntent)
    }
}
