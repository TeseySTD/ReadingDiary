package com.example.readingdiary.ui.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.readingdiary.view_models.LoginScreenViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = viewModel(),
    onLoginSuccess: () -> Unit = {}
) {
    val loginState by viewModel.loginState.observeAsState(LoginScreenViewModel.LoginFormState())

    // Handle UI events from the ViewModel
    LaunchedEffect(true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is LoginScreenViewModel.LoginUiEvent.LoginSuccess -> {
                    onLoginSuccess()
                }
                is LoginScreenViewModel.LoginUiEvent.ShowError -> {
                    // Show error message (could use a snackbar)
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
            value = loginState.login,
            onValueChange = { viewModel.updateLogin(it) },
            label = { Text("Login") }
        )
        TextField(
            value = loginState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { viewModel.login() },
            enabled = loginState.isFormValid
        ) {
            Text(text = "Login", fontSize = 16.sp)
        }
    }
}