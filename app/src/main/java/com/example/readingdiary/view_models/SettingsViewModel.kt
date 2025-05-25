package com.example.readingdiary.view_models


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.readingdiary.models.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val store = SettingsDataStore(application)

    // Перетворюємо Flow у StateFlow для зручності в Compose
    val notifications: StateFlow<Boolean> = store.notificationsFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val sound: StateFlow<Boolean> = store.soundFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val language: StateFlow<String> = store.languageFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, "uk")

    fun toggleNotifications() {
        viewModelScope.launch { store.setNotifications(!notifications.value) }
    }

    fun toggleSound() {
        viewModelScope.launch { store.setSound(!sound.value) }
    }

    fun setLanguage(code: String) {
        viewModelScope.launch { store.setLanguage(code) }
    }
}
