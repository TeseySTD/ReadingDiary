package com.example.readingdiary.ui.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.readingdiary.models.Routes

@Composable
fun BottomNavBar(
    navController: NavHostController,
    windowSize: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
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
    when(windowSize.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { screen ->
                NavigationBarItem(
                    modifier = Modifier.semantics { contentDescription = screen.label },
                    icon = { Icon(screen.icon, contentDescription = screen.label) },
                    label = { Text(screen.label) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route)
                    }
                )
            }
        }
        WindowWidthSizeClass.MEDIUM -> {
            val scrollState = rememberScrollState()
            NavigationRail {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Column(
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    items.forEach { screen ->
                        NavigationRailItem(
                            modifier = Modifier.semantics { contentDescription = screen.label },
                            icon = { Icon(screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) },
                            selected = currentRoute == screen.route,
                            onClick = { navController.navigate(screen.route) }
                        )
                    }
                }
            }
        }

        WindowWidthSizeClass.EXPANDED -> {
            val scrollState = rememberScrollState()
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet {
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .padding(top = 16.dp)
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route
                            items.forEach { screen ->
                                NavigationDrawerItem(
                                    modifier = Modifier.semantics { contentDescription = screen.label },
                                    icon = { Icon(screen.icon, contentDescription = screen.label) },
                                    label = { Text(screen.label) },
                                    selected = currentRoute == screen.route,
                                    onClick = { navController.navigate(screen.route) }
                                )
                            }
                        }
                    }
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Application content")
                    }
                }
            )
        }
    }

}