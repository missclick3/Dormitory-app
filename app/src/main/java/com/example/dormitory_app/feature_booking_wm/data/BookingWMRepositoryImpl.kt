package com.example.dormitory_app.feature_booking_wm.data

import android.content.SharedPreferences
import android.util.Log
import com.example.dormitory_app.feature_booking_wm.domain.BookingWMRepository
import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import com.example.dormitory_app.feature_booking_wm.messages.dtos.WashingMachineDto
import com.example.dormitory_app.feature_booking_wm.messages.requests.TimeRangeRequest
import com.example.dormitory_app.feature_booking_wm.messages.requests.WmNumRequest
import com.example.dormitory_app.feature_booking_wm.messages.responses.ActiveBookings
import com.example.dormitory_app.feature_booking_wm.messages.responses.GetBookingInfoForDormitory
import com.example.dormitory_app.feature_booking_wm.presentation.BookingResult
import retrofit2.HttpException

class BookingWMRepositoryImpl(
    private val prefs: SharedPreferences,
    private val api: BookingApi
) : BookingWMRepository{
    override suspend fun getAllInfo(): BookingResult {
        return try {
            val token = prefs.getString("jwt", null) ?: return BookingResult.Unauthorized
            val response = api.getAllInfo("Bearer $token")
            BookingResult.BookingMainPage(response)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                BookingResult.Unauthorized
            }
            else {
                BookingResult.Error(e.message ?: "FATAL")
            }
        } catch (e: Exception) {
            BookingResult.Error(e.message ?: "FATAL")
        }
    }

    override suspend fun getTimeRange(id: String): BookingResult {
        return try {
            val token = prefs.getString("jwt", null) ?: return BookingResult.Unauthorized
            val response = api.getTimeRange(
                "Bearer $token", id
            )
            BookingResult.Booking(response)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                BookingResult.Unauthorized
            }
            else BookingResult.Error(e.message ?: "FATAL")
        } catch (e: Exception) {
            BookingResult.Error(e.message ?: "FATAL")
        }
    }

    override suspend fun getActiveBookings(): BookingResult {
        return try {
            val token = prefs.getString("jwt", null) ?: return BookingResult.Unauthorized
            val response = api.getActiveBookingsForUser("Bearer $token")
            BookingResult.UsersBookings(response)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                BookingResult.Unauthorized
            }
            else BookingResult.Error(e.message ?: "FATAL")
        } catch (e: Exception) {
            BookingResult.Error(e.message ?: "FATAL")
        }
    }

    override suspend fun bookWM(request: TimeRangeRequest): BookingResult {
        return try {
            val token = prefs.getString("jwt", null) ?: return BookingResult.Unauthorized
            api.bookWM("Bearer $token", request)
            BookingResult.Success("Стиральная машина забронирована")
        } catch (e: HttpException) {
            if (e.code() == 401) {
                BookingResult.Unauthorized
            }
            else BookingResult.Error(e.message ?: "FATAL")
        } catch (e: Exception) {
            BookingResult.Error(e.message ?: "FATAL")
        }
    }

    override suspend fun deleteBooking(id: String): BookingResult {
        return try {
            val token = prefs.getString("jwt", null) ?: return BookingResult.Unauthorized
            api.deleteBooking("Bearer $token", id)
            BookingResult.Success("Бронь удалена")
        } catch (e: HttpException) {
            if (e.code() == 401) {
                BookingResult.Unauthorized
            }
            else BookingResult.Error(e.message ?: "FATAL")
        } catch (e: Exception) {
            BookingResult.Error(e.message ?: "FATAL")
        }
    }

    override suspend fun getWmsForDormitory(): BookingResult {
        return try {
            val token = prefs.getString("jwt", null) ?: return BookingResult.Unauthorized
            val response = api.getWmsForDormitory("Bearer $token")
            BookingResult.ListWM(response)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                BookingResult.Unauthorized
            }
            else BookingResult.Error(e.message ?: "FATAL")
        } catch (e: Exception) {
            BookingResult.Error(e.message ?: "FATAL")
        }
    }

    override suspend fun addWm(wmNumber: Int): BookingResult {
        return try {
            val token = prefs.getString("jwt", null) ?: return BookingResult.Unauthorized
            api.addWm("Bearer $token", WmNumRequest(wmNumber))
            BookingResult.Success("Стиральная машина добавлена")
        } catch (e: HttpException) {
            if (e.code() == 401) {
                BookingResult.Unauthorized
            }
            else BookingResult.Error(e.message ?: "FATAL")
        } catch (e: Exception) {
            BookingResult.Error(e.message ?: "FATAL")
        }
    }

    override suspend fun changeWmStatus(wmNumber: Int): BookingResult {
        return try {
            val token = prefs.getString("jwt", null) ?: return BookingResult.Unauthorized
            api.changeWmStatus("Bearer $token", WmNumRequest(wmNumber))
            BookingResult.Success("Статус стиральной машины изменен")
        } catch (e: HttpException) {
            if (e.code() == 401) {
                BookingResult.Unauthorized
            }
            else BookingResult.Error(e.message ?: "FATAL")
        } catch (e: Exception) {
            BookingResult.Error(e.message ?: "FATAL")
        }
    }

    override suspend fun deleteWM(wmNumber: Int): BookingResult {
        return try {
            val token = prefs.getString("jwt", null) ?: return BookingResult.Unauthorized
            api.deleteWm("Bearer $token", WmNumRequest(wmNumber))
            BookingResult.Success("Стиральная машина была удалена")
        } catch (e: HttpException) {
            Log.e("Error1", e.message ?: "FATAL")
            if (e.code() == 401) {
                BookingResult.Unauthorized
            }
            else BookingResult.Error(e.message ?: "FATAL")
        } catch (e: Exception) {
            Log.e("Error2", e.message ?: "FATAL")
            BookingResult.Error(e.message ?: "FATAL")
        }
    }

    override suspend fun getRole(): BookingResult {
        return try {
            val token = prefs.getString("jwt", null) ?: return BookingResult.Unauthorized
            val response = api.getUser("Bearer $token").userDTO.role
            Log.d("ROLE", response)
            BookingResult.Role(response)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                BookingResult.Unauthorized
            }
            else BookingResult.Error(e.message ?: "FATAL")
        } catch (e: Exception) {
            BookingResult.Error(e.message ?: "FATAL")
        }
    }
}