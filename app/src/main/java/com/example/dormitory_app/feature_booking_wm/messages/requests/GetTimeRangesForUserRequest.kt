package com.example.dormitory_app.feature_booking_wm.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetTimeRangesForUserRequest(
    val userId: String,
    val date: String
)
