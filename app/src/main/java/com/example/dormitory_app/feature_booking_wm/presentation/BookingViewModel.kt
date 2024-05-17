package com.example.dormitory_app.feature_booking_wm.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dormitory_app.feature_booking_wm.domain.BookingWMRepository
import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import com.example.dormitory_app.feature_booking_wm.messages.requests.TimeRangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val repository: BookingWMRepository
) : ViewModel(){

    private val resultChannel = Channel<BookingResult>()
    val bookingResult = resultChannel.receiveAsFlow()
    fun onEvent(event: BookingUIEvent) {
        when (event) {
            is BookingUIEvent.AddWm -> addWm(event.num)
            is BookingUIEvent.BookTimeRange -> bookTimeRange(event.request)
            is BookingUIEvent.ChangeWmStatus -> changeStatus(event.num)
            is BookingUIEvent.DeleteBooking -> deleteBooking(event.id)
            is BookingUIEvent.DeleteWm -> deleteWm(event.num)
            BookingUIEvent.GetRole -> getRole()
            BookingUIEvent.InitMain -> getInfo()
            BookingUIEvent.InitTimeRanges -> getActiveBookings()
            BookingUIEvent.InitWms -> getWms()
            is BookingUIEvent.OpenTimeRange -> getTimeRange(event.id)
        }
    }

    private fun getTimeRange(id: String) {
        viewModelScope.launch {
            val response = repository.getTimeRange(id)
            resultChannel.send(response)
        }
    }

    private fun getWms() {
        viewModelScope.launch {
            resultChannel.send(BookingResult.Loading)
            val response = repository.getWmsForDormitory()
            resultChannel.send(response)
        }
    }

    private fun getActiveBookings() {
        viewModelScope.launch {
            resultChannel.send(BookingResult.Loading)
            val response = repository.getActiveBookings()
            resultChannel.send(response)
        }
    }

    private fun getInfo() {
        viewModelScope.launch {
            resultChannel.send(BookingResult.Loading)
            val response = repository.getAllInfo()
            resultChannel.send(response)
        }
    }

    private fun getRole() {
        viewModelScope.launch {
            val response = repository.getRole()
            resultChannel.send(response)
        }
    }

    private fun deleteWm(num: Int) {
        viewModelScope.launch {
            val response = repository.deleteWM(num)
            resultChannel.send(response)
        }
    }

    private fun deleteBooking(id: String) {
        viewModelScope.launch {
            val response = repository.deleteBooking(id)
            resultChannel.send(response)
        }
    }

    private fun changeStatus(num: Int) {
        viewModelScope.launch {
            val response = repository.changeWmStatus(num)
            resultChannel.send(response)
        }
    }

    private fun addWm(num: Int) {
        viewModelScope.launch {
            val response = repository.addWm(num)
            resultChannel.send(response)
        }
    }

    private fun bookTimeRange(request: TimeRangeRequest) {
        viewModelScope.launch {
            val response = repository.bookWM(request)
            resultChannel.send(response)
        }
    }
}