package com.example.readingdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.compose.AppTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.readingdiary.ui.compose.HomeScreen
import com.example.readingdiary.ui.compose.LoginScreen
import com.example.readingdiary.ui.compose.RegistrationScreen
import com.example.readingdiary.ui.compose.components.AddBookDialogCompose
import com.example.readingdiary.ui.compose.components.AddNoteDialogCompose
import com.example.readingdiary.ui.compose.screens.BooksScreen
import com.example.readingdiary.ui.compose.screens.NotesScreen
import com.example.readingdiary.ui.compose.screens.PlansScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}

enum class TabScreen {
    Home,
    Books,
    Notes,
    Plans,
    Login,
    Registration
}

@Composable
fun MainScreen() {
    var currentTab by remember { mutableIntStateOf(TabScreen.Home.ordinal) }
    var showAddBookDialog by remember { mutableStateOf(false) }
    var showAddNoteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = currentTab) {
                TabScreen.values().forEachIndexed { index, tabScreen ->
                    Tab(
                        selected = currentTab == index,
                        onClick = { currentTab = index },
                        text = {
                            Text(
                                text = when (tabScreen) {
                                    TabScreen.Home -> "Home"
                                    TabScreen.Books -> "Books"
                                    TabScreen.Notes -> "Notes"
                                    TabScreen.Plans -> "Plans"
                                    TabScreen.Login -> "Login"
                                    TabScreen.Registration -> "Register"
                                }
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if (currentTab == TabScreen.Books.ordinal || currentTab == TabScreen.Notes.ordinal) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ){

                    FloatingActionButton(
                        onClick = {
                            when (currentTab) {
                                TabScreen.Books.ordinal -> showAddBookDialog = true
                                TabScreen.Notes.ordinal -> showAddNoteDialog = true
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_input_add),
                            contentDescription = "Add"
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentTab) {
                TabScreen.Home.ordinal -> HomeScreen()
                TabScreen.Books.ordinal -> BooksScreen()
                TabScreen.Notes.ordinal -> NotesScreen()
                TabScreen.Plans.ordinal -> PlansScreen()
                TabScreen.Login.ordinal -> LoginScreen()
                TabScreen.Registration.ordinal -> RegistrationScreen()
            }
        }

        if (showAddBookDialog) {
            AddBookDialogCompose(onDismiss = { showAddBookDialog = false })
        }

        if (showAddNoteDialog) {
            AddNoteDialogCompose(onDismiss = { showAddNoteDialog = false })
        }
    }
}
