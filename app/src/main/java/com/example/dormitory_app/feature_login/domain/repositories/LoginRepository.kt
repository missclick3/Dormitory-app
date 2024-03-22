package com.example.dormitory_app.feature_login.domain.repositories

import com.example.dormitory_app.feature_login.data.messages.AuthRequest
import com.example.dormitory_app.feature_login.domain.AuthResult

interface LoginRepository {
    suspend fun signIn(request: AuthRequest): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}