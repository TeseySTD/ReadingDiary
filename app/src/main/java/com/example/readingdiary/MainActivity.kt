package com.example.readingdiary

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.readingdiary.models.Routes
import com.example.readingdiary.ui.compose.components.AddBookDialogCompose
import com.example.readingdiary.ui.compose.components.AddNoteDialogCompose
import com.example.readingdiary.ui.compose.components.BottomNavBar
import com.example.readingdiary.ui.compose.components.TopNavBar
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.foundation.layout.widthIn
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.readingdiary.ui.compose.screens.BooksScreen
import com.example.readingdiary.ui.compose.screens.HomeScreen
import com.example.readingdiary.ui.compose.screens.LoginScreen
import com.example.readingdiary.ui.compose.screens.NotesScreen
import com.example.readingdiary.ui.compose.screens.PlansScreen
import com.example.readingdiary.ui.compose.screens.RegistrationScreen
import com.example.readingdiary.ui.compose.screens.SettingsScreen
import com.example.readingdiary.ui.screens.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.println(Log.INFO,"OnStart","Main activity started")
    }

    override fun onStop() {
        super.onStop()
        Log.println(Log.INFO,"OnStop","Main activity stopped")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.println(Log.INFO,"OnDestroy","Main activity destroyed")
    }

    override fun onPause() {
        super.onPause()
        Log.println(Log.INFO,"OnPause","Main activity paused")
    }

    override fun onResume() {
        super.onResume()
        Log.println(Log.INFO,"OnResume","Main activity resumed")
    }
}

@Composable
fun MainApp() {
    val windowSize = currentWindowAdaptiveInfo().windowSizeClass
    val navController = rememberNavController()
    var showAddBookDialog by remember { mutableStateOf(false) }
    var showAddNoteDialog by remember { mutableStateOf(false) }
    val currentRoute by navController.currentBackStackEntryAsState()
    val route = currentRoute?.destination?.route

    when (windowSize.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> {
            Scaffold(
                bottomBar = { BottomNavBar(navController) },
                topBar = { TopNavBar(navController) },
                floatingActionButton = {
                    if (route == Routes.Books.route || route == Routes.Notes.route) {
                        FloatingActionButton(onClick = {
                            when (route) {
                                Routes.Books.route -> showAddBookDialog = true
                                Routes.Notes.route -> showAddNoteDialog = true
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                        }
                    }
                }
            ) { paddingValues ->
                Box(
                    Modifier
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
        WindowWidthSizeClass.MEDIUM -> {
            // Rail + content
            Row(Modifier.fillMaxSize()) {
                BottomNavBar(navController)
                Divider(modifier = Modifier.fillMaxHeight().width(1.dp))
                Box(Modifier.weight(1f)) {
                    AppNavHost(
                        navController = navController,
                        showAddBookDialog = showAddBookDialog,
                        onDismissBook = { showAddBookDialog = false },
                        showAddNoteDialog = showAddNoteDialog,
                        onDismissNote = { showAddNoteDialog = false }
                    )
                    // FloatingActionButton over content
                    if (route == Routes.Books.route || route == Routes.Notes.route) {
                        FloatingActionButton(
                            onClick = {
                                when (route) {
                                    Routes.Books.route -> showAddBookDialog = true
                                    Routes.Notes.route -> showAddNoteDialog = true
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .zIndex(1f)
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                        }
                    }
                }
            }
        }
        WindowWidthSizeClass.EXPANDED -> {
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet {
                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            val items = listOf(
                                Routes.Home, Routes.Books, Routes.Notes,
                                Routes.Plans, Routes.Profile,
                                Routes.Login, Routes.Registration
                            )
                            items.forEach { screen ->
                                NavigationDrawerItem(
                                    label = { Text(screen.label) },
                                    icon = { Icon(screen.icon, contentDescription = screen.label) },
                                    selected = route == screen.route,
                                    onClick = { navController.navigate(screen.route) },
                                    modifier = Modifier.widthIn(max = 240.dp)
                                )
                            }
                        }
                    }
                }, content = {
                    Box(Modifier.fillMaxSize()) {
                        AppNavHost(
                            navController = navController,
                            showAddBookDialog = showAddBookDialog,
                            onDismissBook = { showAddBookDialog = false },
                            showAddNoteDialog = showAddNoteDialog,
                            onDismissNote = { showAddNoteDialog = false }
                        )
                        if (route == Routes.Books.route || route == Routes.Notes.route) {
                            FloatingActionButton(
                                onClick = {
                                    when (route) {
                                        Routes.Books.route -> showAddBookDialog = true
                                        Routes.Notes.route -> showAddNoteDialog = true
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(32.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                            }
                        }
                    }
                }
            )
        }
        else -> Unit
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
        composable(Routes.Settings.route) { SettingsScreen(onBack = { navController.popBackStack() })}
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
