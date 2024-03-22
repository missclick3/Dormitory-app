package com.example.dormitory_app.feature_login.domain.usecases

import com.example.dormitory_app.feature_login.data.messages.AuthRequest
import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.feature_login.domain.repositories.LoginRepository

class LoginUseCase (
    private val repository: LoginRepository
) {
    suspend fun execute(username: String, password: String): AuthResult<Unit> {
        val authRequest = AuthRequest(username, password)
        return repository.signIn(authRequest)
    }
}