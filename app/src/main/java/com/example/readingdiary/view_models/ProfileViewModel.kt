package com.example.readingdiary.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readingdiary.models.User
import com.example.readingdiary.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            val currentUser = UserService.user

            _uiState.value = if (currentUser != null && UserService.isAuthenticated(currentUser)) {
                ProfileUiState.Success(currentUser)
            } else {
                ProfileUiState.NotAuthenticated
            }
        }
    }

    fun logout() {
        _uiState.value = ProfileUiState.NotAuthenticated
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User) : ProfileUiState()
    object NotAuthenticated : ProfileUiState()
}