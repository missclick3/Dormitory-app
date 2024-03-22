package com.example.dormitory_app.feature_login.presentation

sealed class AuthUIEvent {
    data class SignInUsernameChanged(val value: String): AuthUIEvent()
    data class SignInPasswordChanged(val value: String): AuthUIEvent()
    data object SignIn: AuthUIEvent()
}