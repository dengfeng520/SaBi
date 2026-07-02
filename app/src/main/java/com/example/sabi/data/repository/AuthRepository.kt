package com.example.sabi.data.repository

import com.example.sabi.data.datasource.RemoteAuthDataSource
import com.example.sabi.data.model.LoginRequest
import com.example.sabi.data.model.LoginResult
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val remoteDataSource: RemoteAuthDataSource
) {
    suspend fun login(username: String, password: String): LoginResult {
        // 可在此添加数据校验、缓存等逻辑
        val request = LoginRequest(username, password)
        return remoteDataSource.login(request)
    }
}