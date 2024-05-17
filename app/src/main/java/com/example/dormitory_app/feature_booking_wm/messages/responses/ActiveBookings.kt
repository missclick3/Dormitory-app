package com.example.dormitory_app.feature_booking_wm.messages.responses

import kotlinx.serialization.Serializable
import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto

@Serializable
data class ActiveBookings(
    val todayBookings: List<TimeRangeDto>,
    val tomorrowBookings: List<TimeRangeDto>
)
