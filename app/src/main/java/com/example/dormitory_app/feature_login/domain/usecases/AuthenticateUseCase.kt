package com.example.dormitory_app.feature_login.domain.usecases

import com.example.dormitory_app.feature_login.data.messages.AuthRequest
import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.feature_login.domain.repositories.LoginRepository

class AuthenticateUseCase(
    private val repository: LoginRepository
) {
    suspend fun execute(): AuthResult<Unit> {
        return repository.authenticate()
    }
}
