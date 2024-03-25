package com.example.dormitory_app.feature_profile.data.messages.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CertificateDTO(
    val id: String? = "",
    val startDate: String,
    val expireDate: String
)