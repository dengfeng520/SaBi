package com.example.sabi.data.datasource

import com.example.sabi.data.model.LoginRequest
import com.example.sabi.data.model.LoginResult
import com.example.sabi.data.model.UserModel
import kotlinx.coroutines.delay

// 模拟远程数据源（实际项目可替换为 Retrofit 网络请求）
class RemoteAuthDataSource {

    suspend fun login(request: LoginRequest): LoginResult {
        // 模拟网络延迟
        delay(1500)

        // 模拟简单验证逻辑
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
                LoginResult.Error("密码长度不能少于 6 位")
            }
        } else {
            LoginResult.Error("用户名或密码不能为空")
        }
    }
}