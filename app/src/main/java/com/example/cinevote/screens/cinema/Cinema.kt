package com.example.cinevote.screens.cinema


import android.Manifest
import android.content.Intent
import com.example.cinevote.R
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cinevote.components.TopBar
import com.example.cinevote.util.LocationService
import com.example.cinevote.util.PermissionStatus
import com.example.cinevote.util.StartMonitoringResult
import com.example.cinevote.util.rememberPermission
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay


@Composable
fun CinemaScreen(
    navController : NavHostController,
    state: CinemaStatus,
    action: CinemaAction,
) {
    var locationService: LocationService = LocationService(LocalContext.current)

    val snackbarHostState = remember { SnackbarHostState() }
    var showLocationDisabledAlert by remember { mutableStateOf(false) }
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

    val locationPermission = rememberPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) { status ->
        when (status) {
            PermissionStatus.Granted -> {

                val res = locationService.requestCurrentLocation()
                //Log.d("coordinates", (locationService.coordinates?.latitude?:0.00).toString())
                showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
            }

            PermissionStatus.Denied ->
                showPermissionDeniedAlert = true

            PermissionStatus.PermanentlyDenied ->
                showPermissionPermanentlyDeniedSnackbar = true

            PermissionStatus.Unknown -> {}
        }
    }
    fun requestLocation() {
        if (locationPermission.status.isGranted) {
            val res = locationService.requestCurrentLocation()
            showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = { TopBar(navController = navController, title = stringResource(id = R.string.cinema_title)) },
        modifier = Modifier.fillMaxWidth(),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            item {

                Button(
                    onClick = { requestLocation() }
                ){
                    Text(text = "Load cinema")
                }

                val latitude = locationService.coordinates?.latitude ?: 0.00
                val longitude = locationService.coordinates?.longitude ?: 0.00
                Log.d("coordinates", latitude.toString())


                if(locationPermission.status.isGranted){
                    if(latitude==0.00 && longitude==0.00){
                        requestLocation()
                    }
                }


                //requestLocation()

                if (latitude != 0.00 && longitude != 0.00) {
                    action.setUserPosition(LatLng(latitude, longitude))

                    action.getCinema(LatLng(latitude, longitude))
                }


                //action.setUserPosition(LatLng(.coordinates?.latitude?:0.00, locationService.coordinates?.longitude?:0.00))
                val ctx = LocalContext.current


                //actions.invoke()






                if (state.cinemaList.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.Nessun_cinema),
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



        if (showLocationDisabledAlert) {
            AlertDialog(
                title = { Text("Location disabled") },
                text = { Text("Location must be enabled to get your current location in the app.") },
                confirmButton = {
                    TextButton(onClick = {
                        locationService.openLocationSettings()
                        showLocationDisabledAlert = false
                    }) {
                        Text("Enable")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLocationDisabledAlert = false }) {
                        Text("Dismiss")
                    }
                },
                onDismissRequest = { showLocationDisabledAlert = false }
            )
        }

        if (showPermissionDeniedAlert) {
            AlertDialog(
                title = { Text("Location permission denied") },
                text = { Text("Location permission is required to get your current location in the app.") },
                confirmButton = {
                    TextButton(onClick = {
                        locationPermission.launchPermissionRequest()
                        showPermissionDeniedAlert = false
                    }) {
                        Text("Grant")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPermissionDeniedAlert = false }) {
                        Text("Dismiss")
                    }
                },
                onDismissRequest = { showPermissionDeniedAlert = false }
            )
        }

        val ctx = LocalContext.current
        if (showPermissionPermanentlyDeniedSnackbar) {
            LaunchedEffect(snackbarHostState) {
                val res = snackbarHostState.showSnackbar(
                    "Location permission is required.",
                    "Go to Settings",
                    duration = SnackbarDuration.Long
                )
                if (res == SnackbarResult.ActionPerformed) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", ctx.packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    if (intent.resolveActivity(ctx.packageManager) != null) {
                        ctx.startActivity(intent)
                    }
                }
                showPermissionPermanentlyDeniedSnackbar = false
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


