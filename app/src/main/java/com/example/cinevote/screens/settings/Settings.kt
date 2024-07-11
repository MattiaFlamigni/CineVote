package com.example.cinevote.screens.settings

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R
import com.example.cinevote.components.TopBar
import com.example.cinevote.screens.auth.AuthStatus
import com.example.cinevote.screens.auth.AuthViewModel
import com.example.cinevote.screens.signUp.firebaseAuth
import com.example.cinevote.util.LoadThumbnail
import com.example.cinevote.util.PermissionStatus
import com.example.cinevote.util.rememberPermission
@Composable
fun SettingsScreen(
    navController: NavHostController,
    state: SettingsStatus,
    action: SettingsAction,
    auth: AuthViewModel
) {
    val email = firebaseAuth.currentUser?.email ?: ""
    LaunchedEffect(email) {
        action.getProfilePic(email)
    }
    action.getFilmReviewd()

    Scaffold(
        topBar = { TopBar(title = "Impostazioni", navController = navController) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    ShowProfile(state, action, null)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Visti",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 100.dp),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            modifier = Modifier.height(300.dp)
                        ) {
                            items(state.watchedMovie) { movie ->
                                MovieItem(movie, navController)
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    ShowOption(action = action, navController = navController, auth = auth)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun ShowProfile(state: SettingsStatus, action: SettingsAction, profileImageUri: Uri?) {


    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

    val readImagePermission = rememberPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) { status ->
        when (status) {
            PermissionStatus.Granted -> {
                // Do nothing, permission is granted
            }
            PermissionStatus.Denied ->
                showPermissionDeniedAlert = true
            PermissionStatus.PermanentlyDenied ->
                showPermissionPermanentlyDeniedSnackbar = true
            PermissionStatus.Unknown -> {}
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            action.updateProfileImage(uri)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .size(120.dp)
                .padding(16.dp)
                .clickable {
                    if (readImagePermission.status.isGranted) {
                        launcher.launch("image/*")
                    } else {
                        readImagePermission.launchPermissionRequest()
                    }
                }
        ) {
            if (state.path.toString().isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile Image",
                    modifier = Modifier.size(120.dp)
                )
            } else {
                LoadThumbnail(state.path.toString(), false)
            }
        }

        Text(
            text = state.username,
            fontFamily = FontFamily.SansSerif,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = state.name,
            fontFamily = FontFamily.SansSerif,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )

        Button(
            onClick = { /*TODO*/ },
            enabled = state.username == "Utente non registrato",
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Edit profile")
        }
    }

    if (showPermissionDeniedAlert) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedAlert = false },
            title = { Text("Permission Denied") },
            text = { Text("The permission to read images was denied. Please grant the permission to select a profile image.") },
            confirmButton = {
                TextButton(onClick = { showPermissionDeniedAlert = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (showPermissionPermanentlyDeniedSnackbar) {
        Snackbar(
            action = {
                TextButton(onClick = { showPermissionPermanentlyDeniedSnackbar = false }) {
                    Text("Settings")
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Permission permanently denied. Please enable it in settings.")
        }
    }
}


@Composable
fun ShowOption(action: SettingsAction, navController: NavHostController, auth: AuthViewModel) {
    val options = listOf(SettingItem.IMPOSTAZIONI_TEMA, SettingItem.LOGOUT, SettingItem.BADGE)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        options.forEach { option ->
            TextButton(
                onClick = {
                    when (option) {
                        SettingItem.LOGOUT -> {
                            action.logOut()
                            auth.changeState(AuthStatus.LOGIN)

                            navController.navigate(NavigationRoute.Login.route)
                        }

                        SettingItem.IMPOSTAZIONI_TEMA -> navController.navigate(NavigationRoute.ThemeScreen.route)
                        SettingItem.RECENSIONI -> TODO()
                        SettingItem.BADGE -> navController.navigate(NavigationRoute.BadgeScreen.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = option.label,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
/*
@Composable
fun MovieItem(movie: WatchedMovie) {

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 100.dp) ) {
        item() {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.posterUrl)
                        .size(Size.ORIGINAL)
                        .build()
                ),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
        }
    }





    /*Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { /* azione da eseguire al click sul film */ }
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current)
                .data(movie.posterUrl)
                .size(Size.ORIGINAL)
                .build()),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp)
        )
    }*/
}*/@Composable
fun MovieItem(movie: WatchedMovie, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f) // Aspect ratio 1:1 for square grid items
            .clickable { navController.navigate(NavigationRoute.Detail.buildRoute(movie.title)) }
            .size(100.dp)
    ) {
        Image(
            painter = rememberImagePainter(
                data = movie.posterUrl,
                builder = {
                    crossfade(true) // Crossfade animation between images
                }
            ),
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(150.dp)
        )
    }
}





