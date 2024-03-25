package com.example.dormitory_app.feature_profile.data.messages.responses

import com.example.dormitory_app.feature_profile.data.messages.dtos.CertificateDTO
import com.example.dormitory_app.feature_profile.data.messages.dtos.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val userDTO: UserDTO,
    val fluoroCertificateDTO: CertificateDTO?,
    val stdsCertificateDTO: CertificateDTO?
)
