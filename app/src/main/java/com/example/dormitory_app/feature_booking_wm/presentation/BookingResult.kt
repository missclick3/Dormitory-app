package com.example.dormitory_app.feature_booking_wm.presentation

import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import com.example.dormitory_app.feature_booking_wm.messages.dtos.WashingMachineDto
import com.example.dormitory_app.feature_booking_wm.messages.responses.ActiveBookings
import com.example.dormitory_app.feature_booking_wm.messages.responses.GetBookingInfoForDormitory

sealed class BookingResult {
    data object Unauthorized: BookingResult()
    data object Loading: BookingResult()
    data class Success(val message: String): BookingResult()
    data class Error(val errorText: String) : BookingResult()
    data class BookingMainPage(val info: List<GetBookingInfoForDormitory>) : BookingResult()
    data class Booking(val booking: TimeRangeDto) : BookingResult()
    data class UsersBookings(val bookings: ActiveBookings) : BookingResult()
    data class ListWM(val wms: List<WashingMachineDto>) : BookingResult()
    data class Role(val role: String) : BookingResult()
}