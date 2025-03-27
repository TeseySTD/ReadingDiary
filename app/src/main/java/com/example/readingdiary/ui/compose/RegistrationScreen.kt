package com.example.readingdiary.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistrationScreen() {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var acceptedPolicy by remember { mutableStateOf(false) }
    val isButtonEnabled = login.isNotBlank() &&
            password.isNotBlank() &&
            birthDate.isNotBlank() &&
            acceptedPolicy

    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        TextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Birth Date (YYYY-MM-DD)") }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Accept Privacy Policy", color = colors.onSurface)
            Switch(
                checked = acceptedPolicy,
                onCheckedChange = { acceptedPolicy = it }
            )
        }
        Button(
            onClick = {
                Log.d(
                    "RegistrationScreen",
                    "Login: $login, Password: $password, Birth Date: $birthDate, Accepted: $acceptedPolicy"
                )
            },
            enabled = isButtonEnabled
        ) {
            Text(text = "Register", fontSize = 16.sp)
        }
    }
}