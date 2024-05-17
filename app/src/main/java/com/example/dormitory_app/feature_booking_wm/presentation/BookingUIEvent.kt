package com.example.dormitory_app.feature_booking_wm.presentation

import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import com.example.dormitory_app.feature_booking_wm.messages.requests.TimeRangeRequest

sealed class BookingUIEvent {
    data object InitMain : BookingUIEvent()
    data object InitWms: BookingUIEvent()
    data object GetRole: BookingUIEvent()
    data object InitTimeRanges : BookingUIEvent()
    data class OpenTimeRange(val id: String) : BookingUIEvent()
    data class BookTimeRange(val request: TimeRangeRequest) : BookingUIEvent()
    data class DeleteBooking(val id : String) : BookingUIEvent()
    data class AddWm(val num : Int) : BookingUIEvent()
    data class DeleteWm(val num : Int) : BookingUIEvent()
    data class ChangeWmStatus(val num : Int) : BookingUIEvent()
}