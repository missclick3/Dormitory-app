package com.example.dormitory_app.feature_login.domain.usecases

data class AuthUseCases(
    val loginUseCase: LoginUseCase,
    val authenticateUseCase: AuthenticateUseCase
)
