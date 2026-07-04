package com.example.sabi.data.model

// User Information Model
data class UserModel(
    val userId: String = "",
    val userName: String = "",
    val email: String = "",
    val token: String = "",
)

// Login request body
data class LoginRequest(
    val username: String,
    val password: String
)

// Login Response
sealed class LoginResult {
    data class Success(val user: UserModel) : LoginResult()
    data class Error(val message: String) : LoginResult()
    object Loading : LoginResult()
}
