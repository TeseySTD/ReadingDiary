package com.example.readingdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.readingdiary.models.Routes
import com.example.readingdiary.ui.compose.screens.HomeScreen
import com.example.readingdiary.ui.compose.screens.LoginScreen
import com.example.readingdiary.ui.compose.screens.RegistrationScreen
import com.example.readingdiary.ui.compose.components.AddBookDialogCompose
import com.example.readingdiary.ui.compose.components.AddNoteDialogCompose
import com.example.readingdiary.ui.compose.components.BottomNavBar
import com.example.readingdiary.ui.compose.components.TopNavBar
import com.example.readingdiary.ui.compose.screens.BooksScreen
import com.example.readingdiary.ui.compose.screens.NotesScreen
import com.example.readingdiary.ui.compose.screens.PlansScreen
import com.example.readingdiary.ui.screens.ProfileScreen
import java.util.Stack


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
        topBar = { TopNavBar(navController) },
        floatingActionButton = {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
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
        Box(
            modifier = Modifier
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
        composable(Routes.Profile.route) { ProfileScreen(navController) }
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

