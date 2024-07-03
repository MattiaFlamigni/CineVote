package com.example.cinevote.screens.settings

import android.Manifest
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import coil.Coil
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.cinevote.NavigationRoute
import com.example.cinevote.components.TopBar
import com.example.cinevote.R
import com.example.cinevote.screens.signUp.firebaseAuth
import com.example.cinevote.util.LoadThumbnail
import com.example.cinevote.util.PermissionStatus
import com.example.cinevote.util.StartMonitoringResult
import com.example.cinevote.util.rememberPermission
import java.io.InputStream

@Composable
fun SettingsScreen(
    navController : NavHostController,
    state:SettingsStatus,
    action: SettingsAction
){


    firebaseAuth.currentUser?.email?.let { action.getProfilePic(it) }

    Scaffold(

        topBar = { TopBar(title="Impostazioni", navController=navController) }
    ) {innerPadding->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            ShowProfile(state, action, null )
            ShowOption(action=action, navController=navController)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowProfile(state: SettingsStatus, action: SettingsAction, profileImageUri: Uri?) {
    val context = LocalContext.current;
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

    val readImagePermission = rememberPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) { status ->
        when (status) {
            PermissionStatus.Granted -> {
                // Do nothing, permission is granted
            }
            PermissionStatus.Denied -> {
                showPermissionDeniedAlert = true
            }
            PermissionStatus.PermanentlyDenied -> {
                showPermissionPermanentlyDeniedSnackbar = true
            }
            PermissionStatus.Unknown -> {}
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            action.updateProfileImage(uri) // Assuming you have an updateProfileImage function in action
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = {
                if (readImagePermission.status.isGranted) {
                    launcher.launch("image/*")
                } else {
                    readImagePermission.launchPermissionRequest()
                }
            }
        ) {
            val painter = profileImageUri?.let {
                Log.d("ShowProfile", "Loading image from URI: $it")
                val inputStream = context.contentResolver.openInputStream(it)
                rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(inputStream)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.star)
                        .size(Size.ORIGINAL) // Ensure the size is set to original
                        .build()
                )
            } ?: run {
                Log.d("ShowProfile", "Loading placeholder image")
                painterResource(id = R.drawable.ic_launcher_foreground)
            }

            /*Image(
                painter = painter,
                contentDescription = "profile image",
                modifier = Modifier.size(200.dp)
            )*/

            LoadThumbnail(state.path.toString(), false)
        }

        Text(
            text = state.username,
            fontFamily = FontFamily.Default,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = state.name,
            fontFamily = FontFamily.Default,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Button(
            onClick = { /*TODO*/ },
            enabled = state.username == "Utente non registrato",
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
fun getInputStreamFromURI(context: Context, uri: Uri): InputStream? {
    return context.contentResolver.openInputStream(uri)
}

fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri, proj, null, null, null)
        val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        cursor?.getString(column_index ?: -1)
    } finally {
        cursor?.close()
    }
}

@Composable
private fun ShowOption(action: SettingsAction, navController: NavHostController){
    val options = listOf(SettingItem.IMPOSTAZIONI_TEMA, SettingItem.LOGOUT)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        //horizontalArrangement = Arrangement.Center
    ){

        for(option in options){

            TextButton(
                onClick = {
                    when(option){
                        SettingItem.LOGOUT->{
                            action.logOut()
                            navController.navigate(NavigationRoute.Login.route)
                        }

                        SettingItem.IMPOSTAZIONI_TEMA -> TODO()
                    }


                },
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text=option.label,
                    fontFamily = FontFamily.Default,
                    fontSize = 30.sp
                )
            }

        }
    }
}





