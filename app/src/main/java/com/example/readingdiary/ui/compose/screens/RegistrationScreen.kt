package com.example.readingdiary.ui.compose.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readingdiary.view_models.RegistrationScreenViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegistrationScreen(
    viewModel: RegistrationScreenViewModel = viewModel(),
    onRegistrationSuccess: () -> Unit = {}
) {
    val registrationState by viewModel.registrationState.observeAsState(RegistrationScreenViewModel.RegistrationFormState())
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is RegistrationScreenViewModel.RegistrationUiEvent.RegistrationSuccess -> {
                    onRegistrationSuccess()
                }
                is RegistrationScreenViewModel.RegistrationUiEvent.ShowError -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = registrationState.login,
            onValueChange = { viewModel.updateLogin(it) },
            label = { Text("Login") }
        )
        TextField(
            value = registrationState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        TextField(
            value = registrationState.birthDate,
            onValueChange = { viewModel.updateBirthDate(it) },
            label = { Text("Birth Date (YYYY-MM-DD)") }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Accept Privacy Policy", color = colors.onSurface)
            Switch(
                checked = registrationState.policyAccepted,
                onCheckedChange = { viewModel.updatePolicyAcceptance(it) }
            )
        }
        Button(
            onClick = { viewModel.register() },
            enabled = registrationState.isFormValid
        ) {
            Text(text = "Register", fontSize = 16.sp)
        }
    }
}