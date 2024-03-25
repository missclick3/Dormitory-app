package com.example.dormitory_app.feature_profile.data.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class PatchUserPersonalInfoRequest(
    val email: String?,
    val phoneNumber: String?,
    val tgUsername: String?
)
