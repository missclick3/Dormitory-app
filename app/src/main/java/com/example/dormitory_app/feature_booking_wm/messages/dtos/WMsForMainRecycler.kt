package com.example.dormitory_app.feature_booking_wm.messages.dtos

data class WMsForMainRecycler(
    val wmNumber: Int,
    val ranges: List<TimeRangeDto>
)
