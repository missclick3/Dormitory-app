package com.example.dormitory_app.feature_login.data.messages

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)
