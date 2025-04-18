package com.example.readingdiary.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readingdiary.models.User
import com.example.readingdiary.services.UserService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    private val _loginState = MutableLiveData(LoginFormState())
    val loginState: LiveData<LoginFormState> = _loginState

    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent: SharedFlow<LoginUiEvent> = _uiEvent

    fun updateLogin(login: String) {
        _loginState.value = _loginState.value?.copy(login = login)
        validateForm()
    }

    fun updatePassword(password: String) {
        _loginState.value = _loginState.value?.copy(password = password)
        validateForm()
    }

    private fun validateForm() {
        val currentState = _loginState.value ?: return
        val isValid = currentState.login.isNotBlank() && currentState.password.isNotBlank()
        _loginState.value = currentState.copy(isFormValid = isValid)
    }

    fun login() {
        val currentState = _loginState.value ?: return
        if (!currentState.isFormValid) {
            return
        }
        UserService.loginUser(User(currentState.login, currentState.password))

        Log.d("LoginScreenViewModel", "Login: ${currentState.login}, Password: ${currentState.password}")

        viewModelScope.launch {
            _uiEvent.emit(LoginUiEvent.LoginSuccess)
        }
    }

    data class LoginFormState(
        val login: String = "",
        val password: String = "",
        val isFormValid: Boolean = false
    )

    sealed class LoginUiEvent {
        object LoginSuccess : LoginUiEvent()
        data class ShowError(val message: String) : LoginUiEvent()
    }
}