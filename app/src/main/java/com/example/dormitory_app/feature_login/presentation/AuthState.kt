package com.example.dormitory_app.feature_login.presentation

data class AuthState(
    val isLoading: Boolean = false,
    val singInUsername: String = "",
    val signInPassword: String = ""
)