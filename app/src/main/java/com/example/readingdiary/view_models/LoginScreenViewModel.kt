package com.example.readingdiary.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    // Login form state
    private val _loginState = MutableLiveData(LoginFormState())
    val loginState: LiveData<LoginFormState> = _loginState

    // UI events for navigation or showing messages
    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent: SharedFlow<LoginUiEvent> = _uiEvent

    // Update login field
    fun updateLogin(login: String) {
        _loginState.value = _loginState.value?.copy(login = login)
        validateForm()
    }

    // Update password field
    fun updatePassword(password: String) {
        _loginState.value = _loginState.value?.copy(password = password)
        validateForm()
    }

    // Validate the form and update button state
    private fun validateForm() {
        val currentState = _loginState.value ?: return
        val isValid = currentState.login.isNotBlank() && currentState.password.isNotBlank()
        _loginState.value = currentState.copy(isFormValid = isValid)
    }

    // Perform login
    fun login() {
        val currentState = _loginState.value ?: return
        if (!currentState.isFormValid) {
            return
        }

        // Log for demo purposes, in a real app you would call an authentication service
        Log.d("LoginScreenViewModel", "Login: ${currentState.login}, Password: ${currentState.password}")

        // Emit success event (in real app, this would happen after successful authentication)
        viewModelScope.launch {
            _uiEvent.emit(LoginUiEvent.LoginSuccess)
        }
    }

    // Form state data class
    data class LoginFormState(
        val login: String = "",
        val password: String = "",
        val isFormValid: Boolean = false
    )

    // UI events sealed class
    sealed class LoginUiEvent {
        object LoginSuccess : LoginUiEvent()
        data class ShowError(val message: String) : LoginUiEvent()
    }
}