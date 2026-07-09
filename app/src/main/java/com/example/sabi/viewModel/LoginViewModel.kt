package com.example.sabi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sabi.data.model.LoginResult
import com.example.sabi.data.model.UserModel
import com.example.sabi.data.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val user: UserModel? = null
)

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username,
            usernameError = null,
            errorMessage = null
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null,
            errorMessage = null
        )
    }

    fun login() {
        viewModelScope.launch {
            // Validate input
            val currentState = _uiState.value
            val usernameError = if (currentState.username.isBlank()) {
                "Username cannot be empty"
            } else null

            val passwordError = if (currentState.password.isBlank()) {
                "Password cannot be empty"
            } else if (currentState.password.length < 6) {
                "Password must be at least 6 characters"
            } else null

            if (usernameError != null || passwordError != null) {
                _uiState.value = currentState.copy(
                    usernameError = usernameError,
                    passwordError = passwordError,
                    isLoading = false
                )
                return@launch
            }

            // Start loading
            _uiState.value = currentState.copy(
                isLoading = true,
                errorMessage = null,
                isSuccess = false
            )

            // Perform login
            val result = repository.login(
                currentState.username,
                currentState.password
            )

            // Handle result
            when (result) {
                is LoginResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        user = result.user,
                        errorMessage = null
                    )
                }
                is LoginResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message,
                        isSuccess = false
                    )
                }
                is LoginResult.Loading -> {
                    // Already handled
                }
            }
        }
    }

    fun loginWithApple() {
        println("login with apple")
    }
    fun loginWithGoogle() {
        println("login with google")
    }
    fun loginWithMicrosoft() {
        println("login with microsoft")
    }

    fun clickUserAgreementButton() {
        println("click user agreement button")
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}