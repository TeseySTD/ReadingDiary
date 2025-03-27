package com.example.readingdiary.ui.compose

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ExitToApp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.readingdiary.R

@Composable
fun HomeScreen() {
    var statusText by remember { mutableStateOf("") }
    var brightness by remember { mutableStateOf(50f) }
    var notificationsEnabled by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    LaunchedEffect(brightness) {
        (context as? Activity)?.window?.let { window ->
            val layoutParams = window.attributes
            layoutParams.screenBrightness = brightness / 100f
            window.attributes = layoutParams
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.book_of_black_cover_closed_svgrepo_com),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .alpha(0.8f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Hello, reader!",
                style = MaterialTheme.typography.headlineSmall,
                color = colors.onSurface
            )
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "What reading todey?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurfaceVariant
                    )
                    TextField(
                        value = statusText,
                        onValueChange = { statusText = it },
                        label = { Text("Enter your status") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surfaceContainer)
            ){
                IconButton(onClick = { showControls = !showControls }) {
                    Icon(
                        imageVector = if (showControls) Icons.Filled.ArrowBack else Icons.Filled.ArrowForward,
                        contentDescription = "Toggle controls",
                        tint = colors.onSurface
                    )
                }
            }

            if (showControls) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = colors.surfaceContainer)
                ){
                    Column {
                        Text(
                            text = "Brightness: ${brightness.toInt()}%",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.onSurface
                        )
                        Slider(
                            value = brightness,
                            onValueChange = { brightness = it },
                            valueRange = 0f..100f,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Messages: ${if (notificationsEnabled) "On" else "Off"}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.onSurface
                        )
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )
                    }
                }
            }
        }
    }
}
