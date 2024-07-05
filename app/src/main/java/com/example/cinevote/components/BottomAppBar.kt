package com.example.cinevote.components


import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R


data class BottomNavigationItem(
    val title: String,
    val iconId: Int, // Resource ID of the drawable icon
    val selectedIconId: Int? = null, // Optional resource ID for selected icon
    val badgeCount: Int = 0,
    val route: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bottomAppBar(navController: NavHostController) {

    val items = listOf(
        BottomNavigationItem(
            title = NavigationRoute.HomeScreen.route,
            iconId = R.drawable.home_24dp_5f6368_fill0_wght400_grad0_opsz24,
            selectedIconId = R.drawable.home_24dp_5f6368_fill1_wght400_grad0_opsz24,
            route = { navController.navigate(NavigationRoute.HomeScreen.route) }
        ),
        BottomNavigationItem(
            title = NavigationRoute.Ricerca.route,
            iconId = R.drawable.search_24dp_5f6368_fill0_wght400_grad0_opsz24,
            route = { navController.navigate(NavigationRoute.Ricerca.route) }
        ),
        BottomNavigationItem(
            title = NavigationRoute.OutNow.route,
            iconId = R.drawable.movie_24dp_5f6368_fill0_wght400_grad0_opsz24,
            selectedIconId = R.drawable.movie_24dp_5f6368_fill1_wght400_grad0_opsz24,
            route = { navController.navigate(NavigationRoute.OutNow.route) }
        ),
        BottomNavigationItem(
            title = NavigationRoute.Cinema.route,
            iconId = R.drawable.location_on_24dp_5f6368_fill0_wght400_grad0_opsz24,
            selectedIconId = R.drawable.location_on_24dp_5f6368_fill1_wght400_grad0_opsz24,
            route = { navController.navigate(NavigationRoute.Cinema.route) }
        ),
    )

    val currentBackStackEntry = navController.currentBackStackEntry
    val selectedRoute = currentBackStackEntry?.destination?.route ?: return
    val selectedIndex = items.indexOfFirst { it.title == selectedRoute }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                onClick = item.route,
                selected = selectedIndex == index,

                label = {
                    Text(text = item.title)
                },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount > 0) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = if (index == selectedIndex && item.selectedIconId != null) {
                                item.selectedIconId!! // Use selected icon if provided
                            } else {
                                item.iconId
                            } ),
                            contentDescription = item.title
                        )
                    }
                }
            )
        }
    }
}






