package com.s8159141.assignment2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s8159141.assignment2.data.repository.AuthRepository
import com.s8159141.assignment2.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** UI state for the Login screen. */
sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val keypass: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

/**
 * Validates input and performs login through [AuthRepository]. The screen observes
 * [uiState] and reacts (shows a spinner, an error, or navigates on success).
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Please enter both username and password.")
            return
        }
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            _uiState.value = when (val result = authRepository.login(username, password)) {
                is Result.Success -> LoginUiState.Success(result.data)
                is Result.Error -> LoginUiState.Error(result.message)
            }
        }
    }

    /** Resets to idle after an error has been shown. */
    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}
