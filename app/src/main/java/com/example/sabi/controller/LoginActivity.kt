package com.example.sabi.controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.sabi.data.datasource.LoginDataSource
import com.example.sabi.data.repository.LoginRepository
import com.example.sabi.ui.theme.SaBiTheme
import com.example.sabi.view.LoginView
import com.example.sabi.viewModel.LoginViewModel

class LoginActivity : ComponentActivity() {
    private val loginDataSource by lazy { LoginDataSource() }
    private val loginRepository by lazy { LoginRepository(loginDataSource) }
    private val loginViewModel by lazy { LoginViewModel(loginRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SaBiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginView(viewModel = loginViewModel)
                }
            }
        }
    }
}