package com.example.readingdiary.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(val route: String, val label: String, val icon: ImageVector) {
    object Home : Routes("home", "Home", Icons.Filled.Home)
    object Books : Routes("books", "Books", Icons.Filled.AccountBox)
    object Notes : Routes("notes", "Notes", Icons.Filled.Edit)
    object Plans : Routes("plans", "Plans", Icons.Filled.Star)
    object Profile : Routes("profile","Prof..", Icons.Filled.AccountCircle)
    object Login : Routes("login", "Log..", Icons.Filled.AccountCircle)
    object Registration : Routes("registration", "Reg..", Icons.Filled.AccountCircle)
}
