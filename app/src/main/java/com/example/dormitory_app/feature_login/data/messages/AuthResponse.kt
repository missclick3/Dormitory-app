package com.example.dormitory_app.feature_login.data.messages

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
)
