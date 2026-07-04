package com.example.sabi.data.datasource

import com.example.sabi.data.model.LoginRequest
import com.example.sabi.data.model.LoginResult
import com.example.sabi.data.model.UserModel
import kotlinx.coroutines.delay

// Simulate remote data source (in actual projects, it can be replaced with Retrofit network requests)
class LoginDataSource {

    suspend fun login(request: LoginRequest): LoginResult {
        // Simulate network latency
        delay(1500)

        // Simulate simple verification logic
        return if (request.username.isNotBlank() && request.password.isNotBlank()) {
            if (request.password.length >= 6) {
                LoginResult.Success(
                    UserModel(
                        userId = "123456",
                        userName = request.username,
                        email = "$request.username@example.com",
                        token = "token-${System.currentTimeMillis()}"
                    )
                )
            } else {
                LoginResult.Error("The password length cannot be less than 6 characters")
            }
        } else {
            LoginResult.Error("The username or password cannot be empty")
        }
    }
}