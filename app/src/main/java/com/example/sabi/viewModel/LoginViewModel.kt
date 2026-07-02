package com.example.sabi.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sabi.data.model.LoginResult
import com.example.sabi.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI 状态
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginResult: LoginResult? = null,
    val isUsernameValid: Boolean = true,
    val isPasswordValid: Boolean = true,
    val errorMessage: String? = null
)

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // 使用 StateFlow 管理 UI 状态
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // 处理用户输入事件
    fun onUsernameChange(username: String) {
        _uiState.update { state ->
            state.copy(
                username = username,
                isUsernameValid = username.isNotBlank(),
                errorMessage = null
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { state ->
            state.copy(
                password = password,
                isPasswordValid = password.length >= 6,
                errorMessage = null
            )
        }
    }

    // 登录事件
    fun onLoginClick() {
        val currentState = _uiState.value

        // 输入校验
        if (currentState.username.isBlank()) {
            _uiState.update {
                it.copy(
                    isUsernameValid = false,
                    errorMessage = "请输入用户名"
                )
            }
            return
        }
        if (currentState.password.length < 6) {
            _uiState.update {
                it.copy(
                    isPasswordValid = false,
                    errorMessage = "密码长度不能少于 6 位"
                )
            }
            return
        }

        // 开始登录
        viewModelScope.launch {
            // 设置加载状态
            _uiState.update { it.copy(isLoading = true, loginResult = null) }

            // 调用 Repository 执行登录
            val result = authRepository.login(
                currentState.username,
                currentState.password
            )

            // 更新 UI 状态
            _uiState.update {
                it.copy(
                    isLoading = false,
                    loginResult = result,
                    errorMessage = if (result is LoginResult.Error) result.message else null
                )
            }
        }
    }

    // 清除结果状态（用于导航或重置）
    fun clearResult() {
        _uiState.update { it.copy(loginResult = null, errorMessage = null) }
    }
}