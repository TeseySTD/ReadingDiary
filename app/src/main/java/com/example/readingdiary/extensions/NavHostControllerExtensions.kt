package com.example.readingdiary.extensions

import android.annotation.SuppressLint
import androidx.navigation.NavHostController

class NavHostControllerExtensions {
    companion object{
        fun NavHostController.canNavigateBack():Boolean = this.previousBackStackEntry != null
    }
}