package com.example.dormitory_app.feature_news.messages.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val role: String
)
