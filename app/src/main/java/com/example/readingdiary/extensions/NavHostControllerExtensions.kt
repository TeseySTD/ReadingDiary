package com.example.readingdiary.extensions

import android.annotation.SuppressLint
import androidx.navigation.NavHostController

class NavHostControllerExtensions {
    companion object{
        @SuppressLint("RestrictedApi")
        fun NavHostController.getBackStackLength():Int{
            return this.currentBackStack.value.size
        }

        fun NavHostController.canNavigateBack():Boolean = this.getBackStackLength() > 2
    }
}