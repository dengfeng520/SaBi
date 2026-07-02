package com.example.sabi.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sabi.data.model.LoginResult
import com.example.sabi.viewModel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginView(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (String) -> Unit = {}  // 登录成功回调，用于导航
) {
    val uiState by viewModel.uiState.collectAsState()

    // 监听登录结果，触发导航
    LaunchedEffect(uiState.loginResult) {
        when (val result = uiState.loginResult) {
            is LoginResult.Success -> {
                onLoginSuccess(result.user.token)
                viewModel.clearResult()
            }
            else -> { /* 不做操作，由 UI 展示错误 */ }
        }
    }

    // 登录失败时显示 Snackbar
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            // 实际项目中可在此显示 Snackbar 或 Toast
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // App Logo 或标题
                Text(
                    text = "有一天，「撒币」也会成为一种艺术。",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(48.dp))


                // 错误信息展示
                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    )
}

// 预览
@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    MaterialTheme {
        LoginView()
    }
}