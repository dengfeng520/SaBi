package com.example.sabi.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sabi.R
import com.example.sabi.viewModel.LoginViewModel
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    // 获取系统栏高度
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val navigationBarHeight = with(density) {
        // 获取导航栏高度（如果有的话）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            configuration.densityDpi.let { dpi ->
                // 使用系统资源获取导航栏高度
                val resourceId = androidx.compose.ui.platform.LocalContext.current.resources
                    .getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    androidx.compose.ui.platform.LocalContext.current.resources
                        .getDimensionPixelSize(resourceId).toDp()
                } else {
                    0.dp
                }
            }
        } else {
            0.dp
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding() // 添加系统栏内边距
        ) {
            // 主要登录内容 - 使用可滚动的 Column 防止内容被裁剪
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 32.dp,
                        end = 32.dp,
                        top = 32.dp,
                        bottom = if (navigationBarHeight > 0.dp) {
                            // 为导航栏预留空间 + 用户协议按钮的空间
                            navigationBarHeight + 80.dp
                        } else {
                            80.dp // 默认预留空间
                        }
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Title
                Text(
                    text = "「撒币」",
                    fontSize = 32.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Username Field
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = { viewModel.updateUsername(it) },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.usernameError != null,
                    supportingText = {
                        if (uiState.usernameError != null) {
                            Text(
                                text = uiState.usernameError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.updatePassword(it) },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = uiState.passwordError != null,
                    supportingText = {
                        if (uiState.passwordError != null) {
                            Text(
                                text = uiState.passwordError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    },
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Button(
                    onClick = { viewModel.login() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Login", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Social Login Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SocialLoginButton(
                        iconRes = R.drawable.apple_sigin_icon,
                        contentDescription = "Sign in with Apple",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.loginWithApple() }
                    )

                    SocialLoginButton(
                        iconRes = R.drawable.google_sigin_icon,
                        contentDescription = "Sign in with Google",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.loginWithGoogle() }
                    )

                    SocialLoginButton(
                        iconRes = R.drawable.microsoft_sigin_icon,
                        contentDescription = "Sign in with Microsoft",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.loginWithMicrosoft() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error Message
                if (uiState.errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = uiState.errorMessage!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Success Message
                if (uiState.isSuccess) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Login successful! Welcome ${uiState.user?.userName ?: ""}",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp
                        )
                    }
                }

                // 添加底部空间占位，确保内容不被用户协议按钮遮挡
                Spacer(modifier = Modifier.height(16.dp))
            }

            // User Agreement Button - 固定在底部，考虑导航栏
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = if (navigationBarHeight > 0.dp) {
                            // 为导航栏预留空间
                            navigationBarHeight + 8.dp
                        } else {
                            16.dp
                        }
                    )
            ) {
                TextButton(
                    onClick = {
                        viewModel.clickUserAgreementButton()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 60.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        text = stringResource(R.string.user_agreement),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SocialLoginButton(
    iconRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .padding(0.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors()
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(28.dp)
        )
    }
}