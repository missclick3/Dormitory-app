package com.example.dormitory_app.feature_booking_wm.messages.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val role: String,
)