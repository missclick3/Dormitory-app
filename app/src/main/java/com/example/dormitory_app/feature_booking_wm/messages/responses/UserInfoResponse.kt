package com.example.dormitory_app.feature_booking_wm.messages.responses

import com.example.dormitory_app.feature_booking_wm.messages.dtos.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val userDTO: UserDTO
)
