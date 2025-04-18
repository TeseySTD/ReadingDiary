package com.example.readingdiary.ui.compose.screens
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    var confirmedTitle by rememberSaveable  { mutableStateOf<String?>(null) }
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
        modifier = Modifier.fillMaxSize()
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
            confirmedTitle?.let {
                Text(
                    text = "You are reading: $it",
                    color = colors.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
                        text = "What reading today?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onSurfaceVariant
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = statusText,
                            onValueChange = { statusText = it },
                            label = { Text("Enter your book title") },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            confirmedTitle = statusText
                        }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Confirm",
                                tint = colors.primary
                            )
                        }
                    }

                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = colors.surfaceContainer)
            ) {
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
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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
                                onCheckedChange = {
                                    notificationsEnabled = it
                                    Toast.makeText(
                                        context,
                                        if (it) "Notifications enabled" else "Notifications disabled",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

