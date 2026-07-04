package com.example.sabi.data.repository

import com.example.sabi.data.datasource.LoginDataSource
import com.example.sabi.data.model.LoginRequest
import com.example.sabi.data.model.LoginResult

class LoginRepository constructor(
    private val remoteDataSource: LoginDataSource
) {
    suspend fun login(username: String, password: String): LoginResult {
        // You can add data verification, caching, and other logics here
        val request = LoginRequest(username, password)
        return remoteDataSource.login(request)
    }
}