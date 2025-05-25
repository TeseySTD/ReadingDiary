package com.example.readingdiary.ui.compose.screens

import android.media.AudioManager
import android.media.ToneGenerator
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readingdiary.view_models.SettingsViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current
    val notifications by viewModel.notifications.collectAsState()
    val sound         by viewModel.sound.collectAsState()
    val language      by viewModel.language.collectAsState()

    // Тоногенератор для тестового звуку
    val toneGen = remember { ToneGenerator(AudioManager.STREAM_MUSIC, 100) }

    // Динамічні лейбли за вибором мови
    val labelSettings = if (language == "uk") "Налаштування" else "Settings"
    val labelNotifications = if (language == "uk") "Сповіщення" else "Notifications"
    val labelSound = if (language == "uk") "Звук у додатку" else "App Sound"
    val labelLanguage = if (language == "uk") "Мова додатку" else "Language"
    val labelTestSound = if (language == "uk") "Протестувати звук" else "Test Sound"
    val labelTestNotif = if (language == "uk") "Протестувати сповіщення" else "Test Notification"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // 1. Сповіщення
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(labelNotifications, style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = notifications,
                onCheckedChange = { viewModel.toggleNotifications() }
            )
        }

        // 2. Звук
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(labelSound, style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = sound,
                onCheckedChange = { viewModel.toggleSound() }
            )
        }

        // 3. Мова
        Text(labelLanguage, style = MaterialTheme.typography.bodyLarge)
        val languages = listOf("uk" to "Українська", "en" to "English")
        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(languages.first { it.first == language }.second)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = "Select")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { (code, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            viewModel.setLanguage(code)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка для тестування звуку
        Button(
            onClick = {
                if (sound) {
                    toneGen.startTone(ToneGenerator.TONE_PROP_BEEP)
                } else if(notifications) {
                    Toast.makeText(context,
                        if (language == "uk") "Звук вимкнено" else "Sound disabled",
                        Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(labelTestSound)
        }

        // Кнопка для тестування сповіщень
        Button(
            onClick = {
                if (notifications) {
                    Toast.makeText(context,
                        if (language == "uk") "Це тестове сповіщення" else "This is a test notification",
                        Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(labelTestNotif)
        }
    }
}
