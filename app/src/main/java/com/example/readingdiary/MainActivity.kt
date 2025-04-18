package com.example.readingdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.readingdiary.ui.compose.screens.HomeScreen
import com.example.readingdiary.ui.compose.screens.LoginScreen
import com.example.readingdiary.ui.compose.screens.RegistrationScreen
import com.example.readingdiary.ui.compose.components.AddBookDialogCompose
import com.example.readingdiary.ui.compose.components.AddNoteDialogCompose
import com.example.readingdiary.ui.compose.screens.BooksScreen
import com.example.readingdiary.ui.compose.screens.NotesScreen
import com.example.readingdiary.ui.compose.screens.PlansScreen

sealed class Routes(val route: String, val label: String, val icon: ImageVector) {
    object Home : Routes("home", "Home", Icons.Filled.Home)
    object Books : Routes("books", "Books", Icons.Filled.AccountBox)
    object Notes : Routes("notes", "Notes", Icons.Filled.Edit)
    object Plans : Routes("plans", "Plans", Icons.Filled.Star)
    object Login : Routes("login", "Login", Icons.Filled.Star)
    object Registration : Routes("registration", "Register", Icons.Filled.Star)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    var showAddBookDialog by remember { mutableStateOf(false) }
    var showAddNoteDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute == Routes.Books.route || currentRoute == Routes.Notes.route) {
                FloatingActionButton(
                    onClick = {
                        when (currentRoute) {
                            Routes.Books.route -> showAddBookDialog = true
                            Routes.Notes.route -> showAddNoteDialog = true
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            AppNavHost(
                navController = navController,
                showAddBookDialog = showAddBookDialog,
                onDismissBook = { showAddBookDialog = false },
                showAddNoteDialog = showAddNoteDialog,
                onDismissNote = { showAddNoteDialog = false }
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    showAddBookDialog: Boolean,
    onDismissBook: () -> Unit,
    showAddNoteDialog: Boolean,
    onDismissNote: () -> Unit
) {
    NavHost(navController = navController, startDestination = Routes.Home.route) {
        composable(Routes.Home.route) { HomeScreen() }
        composable(Routes.Books.route) { BooksScreen() }
        composable(Routes.Notes.route) { NotesScreen() }
        composable(Routes.Plans.route) { PlansScreen() }
        composable(Routes.Login.route) { LoginScreen() }
        composable(Routes.Registration.route) { RegistrationScreen() }
    }

    if (showAddBookDialog) {
        AddBookDialogCompose(onDismiss = onDismissBook)
    }
    if (showAddNoteDialog) {
        AddNoteDialogCompose(onDismiss = onDismissNote)
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Routes.Home,
        Routes.Books,
        Routes.Notes,
        Routes.Plans,
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
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
