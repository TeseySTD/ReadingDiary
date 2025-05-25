package com.example.readingdiary.models


import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "user_prefs"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

object SettingsKeys {
    val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
    val SOUND         = booleanPreferencesKey("sound_enabled")
    val LANGUAGE      = stringPreferencesKey("app_language")
}

class SettingsDataStore(private val context: Context) {

    val notificationsFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[SettingsKeys.NOTIFICATIONS] ?: true }

    val soundFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[SettingsKeys.SOUND] ?: true }

    val languageFlow: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[SettingsKeys.LANGUAGE] ?: "uk" }

    suspend fun setNotifications(enabled: Boolean) {
        context.dataStore.edit { it[SettingsKeys.NOTIFICATIONS] = enabled }
    }

    suspend fun setSound(enabled: Boolean) {
        context.dataStore.edit { it[SettingsKeys.SOUND] = enabled }
    }

    suspend fun setLanguage(code: String) {
        context.dataStore.edit { it[SettingsKeys.LANGUAGE] = code }
    }
}
