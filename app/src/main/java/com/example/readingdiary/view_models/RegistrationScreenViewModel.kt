package com.example.readingdiary.view_models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class RegistrationScreenViewModel : ViewModel() {
    // Registration form state
    private val _registrationState = MutableLiveData(RegistrationFormState())
    val registrationState: LiveData<RegistrationFormState> = _registrationState

    // UI events for navigation or showing messages
    private val _uiEvent = MutableSharedFlow<RegistrationUiEvent>()
    val uiEvent: SharedFlow<RegistrationUiEvent> = _uiEvent

    // Update login field
    fun updateLogin(login: String) {
        _registrationState.value = _registrationState.value?.copy(login = login)
        validateForm()
    }

    // Update password field
    fun updatePassword(password: String) {
        _registrationState.value = _registrationState.value?.copy(password = password)
        validateForm()
    }

    // Update birth date field
    fun updateBirthDate(birthDate: String) {
        _registrationState.value = _registrationState.value?.copy(birthDate = birthDate)
        validateForm()
    }

    // Update policy acceptance
    fun updatePolicyAcceptance(accepted: Boolean) {
        _registrationState.value = _registrationState.value?.copy(policyAccepted = accepted)
        validateForm()
    }

    // Validate the form and update form validity state
    private fun validateForm() {
        val currentState = _registrationState.value ?: return

        val isLoginValid = currentState.login.isNotBlank()
        val isPasswordValid = currentState.password.isNotBlank()
        val isBirthDateValid = isValidDateFormat(currentState.birthDate)

        val isFormValid = isLoginValid &&
                isPasswordValid &&
                isBirthDateValid &&
                currentState.policyAccepted

        _registrationState.value = currentState.copy(isFormValid = isFormValid)
    }

    // Validate date format (YYYY-MM-DD)
    private fun isValidDateFormat(date: String): Boolean {
        if (date.isBlank()) return false

        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            formatter.isLenient = false
            formatter.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Perform registration
    fun register() {
        val currentState = _registrationState.value ?: return
        if (!currentState.isFormValid) {
            return
        }

        Log.d("RegistrationScreenViewModel",
            "Login: ${currentState.login}, " +
                    "Password: ${currentState.password}, " +
                    "Birth Date: ${currentState.birthDate}, " +
                    "Accepted: ${currentState.policyAccepted}"
        )

        viewModelScope.launch {
            _uiEvent.emit(RegistrationUiEvent.RegistrationSuccess)
        }
    }

    data class RegistrationFormState(
        val login: String = "",
        val password: String = "",
        val birthDate: String = "",
        val policyAccepted: Boolean = false,
        val isFormValid: Boolean = false
    )

    sealed class RegistrationUiEvent {
        object RegistrationSuccess : RegistrationUiEvent()
        data class ShowError(val message: String) : RegistrationUiEvent()
    }
}