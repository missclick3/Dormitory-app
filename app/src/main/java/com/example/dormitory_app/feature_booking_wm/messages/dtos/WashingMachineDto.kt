package com.example.dormitory_app.feature_booking_wm.messages.dtos

import kotlinx.serialization.Serializable

@Serializable
data class WashingMachineDto(
    val id: String? = "",
    val address: String,
    val wmNumber: Int,
    val enabled: Boolean
)
