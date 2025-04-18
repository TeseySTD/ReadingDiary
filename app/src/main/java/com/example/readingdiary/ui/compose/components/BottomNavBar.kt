package com.example.readingdiary.ui.compose.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.readingdiary.models.Routes

@Composable
fun BottomNavBar(
    navController: NavHostController,
) {
    val items = listOf(
        Routes.Home,
        Routes.Books,
        Routes.Notes,
        Routes.Plans,
        Routes.Profile,
        Routes.Login,
        Routes.Registration
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route)
                }
            )
        }
    }
}