package com.example.cinevote.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title:String="", navController: NavHostController){
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
                    Icon(Icons.Default.ArrowBack, "back")
                }
            }
        },
        actions = {
            if(title!= stringResource(id = R.string.wishlist_title)) {
                IconButton(onClick = { navController.navigate(NavigationRoute.WishList.route) }) {
                    Icon(Icons.Default.Favorite, "Likes")
                }
            }
        }

    )
}