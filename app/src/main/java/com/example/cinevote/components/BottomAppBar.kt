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
import androidx.navigation.NavHostController
import com.example.cinevote.NavigationRoute
import com.example.cinevote.R

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon:ImageVector,
    val badgeCount: Int,
    val route : ()->Unit
)






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bottomAppBar(navController: NavHostController){

    val items = listOf(
        BottomNavigationItem(
            title = NavigationRoute.HomeScreen.route,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            badgeCount=0,
            route = { navController.navigate(NavigationRoute.HomeScreen.route) }
        ),
        BottomNavigationItem(
            title = NavigationRoute.Ricerca.route,
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            badgeCount=0,
            route = { navController.navigate(NavigationRoute.Ricerca.route) }

        ),
        BottomNavigationItem(
            title = NavigationRoute.OutNow.route,
            selectedIcon = Icons.Filled.PlayArrow,
            unselectedIcon = Icons.Outlined.PlayArrow,
            badgeCount=0,
            route = { navController.navigate(NavigationRoute.HomeScreen.route) }
        ),
        BottomNavigationItem(
            title = NavigationRoute.Cinema.route,
            selectedIcon = Icons.Filled.Notifications,
            unselectedIcon = Icons.Outlined.Notifications,
            badgeCount=0,
            route = { navController.navigate(NavigationRoute.Cinema.route) }
        ),
    )


    val currentBackStackEntry = navController.currentBackStackEntry
    val selectedRoute = currentBackStackEntry?.destination?.route ?: return
    val selectedIndex = items.indexOfFirst { it.title == selectedRoute }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(

                onClick = {
                    navController.navigate(item.title)
                },
                selected = selectedIndex == index,

                label = {
                    Text(text = item.title)
                },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        badge = {
                            if(item.badgeCount > 0) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (index == selectedIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title
                        )
                    }
                }
            )
        }
    }
}








