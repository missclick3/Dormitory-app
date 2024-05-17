package com.example.dormitory_app.feature_booking_wm.messages.responses

import kotlinx.serialization.Serializable
import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto

@Serializable
data class GetBookingInfoForDormitory(
    val date: String,
    val wmNumber: Int,
    val timeRangesForToday: List<TimeRangeDto>,
    val timeRangesForTomorrow: List<TimeRangeDto>
)
