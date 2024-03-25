package com.example.dormitory_app.feature_profile.data.messages.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: String? = "",
    val username: String,
    val name: String,
    val role: String,
    val surname: String,
    val patronymic: String? = "",
    val email: String? = "",
    val phoneNumber: String? = "",
    val tgUsername: String? = "",
    val address: String,
    val password: String? = "",
    val salt: String? = ""
)