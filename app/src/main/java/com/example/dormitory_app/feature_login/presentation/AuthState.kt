package com.example.dormitory_app.feature_login.presentation

import android.opengl.Visibility
import android.text.InputType

data class AuthState(
    val isLoading: Boolean = false,
    val singInUsername: String = "",
    val signInPassword: String = ""
)