package com.example.dormitory_app.feature_booking_wm.data

import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import com.example.dormitory_app.feature_booking_wm.messages.dtos.WashingMachineDto
import com.example.dormitory_app.feature_booking_wm.messages.requests.TimeRangeRequest
import com.example.dormitory_app.feature_booking_wm.messages.requests.WmNumRequest
import com.example.dormitory_app.feature_booking_wm.messages.responses.ActiveBookings
import com.example.dormitory_app.feature_booking_wm.messages.responses.GetBookingInfoForDormitory
import com.example.dormitory_app.feature_booking_wm.messages.responses.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface BookingApi {

    @GET("/booking")
    suspend fun getAllInfo(
        @Header("Authorization") token: String
    ) : List<GetBookingInfoForDormitory>

    @GET("/booking/{id}")
    suspend fun getTimeRange(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ) : TimeRangeDto

    @GET("/booking/my")
    suspend fun getActiveBookingsForUser(
        @Header("Authorization") token: String
    ) : ActiveBookings

    @POST("/booking")
    suspend fun bookWM(
        @Header("Authorization") token: String,
        @Body request: TimeRangeRequest
    )

    @DELETE("/booking/{id}")
    suspend fun deleteBooking(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )

    @GET("/booking/admin")
    suspend fun getWmsForDormitory(
        @Header("Authorization") token: String
    ) : List<WashingMachineDto>

    @POST("/booking/admin")
    suspend fun addWm(
        @Header("Authorization") token: String,
        @Body request: WmNumRequest
    )

    @PATCH("/booking/admin")
    suspend fun changeWmStatus(
        @Header("Authorization") token: String,
        @Body request: WmNumRequest
    )

    @HTTP(method = "DELETE", path = "/booking/admin", hasBody = true)
    suspend fun deleteWm(
        @Header("Authorization") token: String,
        @Body request: WmNumRequest
    )

    @GET("/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ) : UserInfoResponse
}