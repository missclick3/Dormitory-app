package com.example.dormitory_app.feature_booking_wm.domain

import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import com.example.dormitory_app.feature_booking_wm.messages.requests.TimeRangeRequest
import com.example.dormitory_app.feature_booking_wm.presentation.BookingResult

interface BookingWMRepository {
    suspend fun getAllInfo() : BookingResult
    suspend fun getTimeRange(id: String) : BookingResult
    suspend fun getActiveBookings() : BookingResult
    suspend fun bookWM(request: TimeRangeRequest) : BookingResult
    suspend fun deleteBooking(id: String) : BookingResult
    suspend fun getWmsForDormitory() : BookingResult
    suspend fun addWm(wmNumber : Int) : BookingResult
    suspend fun changeWmStatus(wmNumber: Int) : BookingResult
    suspend fun deleteWM(wmNumber: Int) : BookingResult
    suspend fun getRole() : BookingResult
}