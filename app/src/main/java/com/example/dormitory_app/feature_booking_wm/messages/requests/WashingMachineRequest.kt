package com.example.dormitory_app.feature_booking_wm.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class WashingMachineRequest(
    val address: String,
    val wmNumber: Int
)
