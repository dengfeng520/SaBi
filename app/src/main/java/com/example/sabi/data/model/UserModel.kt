package com.example.sabi.data.model

// 用户信息Model
data class UserModel(
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val token: String = "",
)

// 登录请求体
data class LoginRequest(
    val username: String,
    val password: String
)

// 登录响应
sealed class LoginResult {
    data class Success(val user: UserModel) : LoginResult()
    data class Error(val message: String) : LoginResult()
    object Loading : LoginResult()
}
