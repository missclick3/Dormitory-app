package com.example.dormitory_app.feature_booking_wm.ui

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.compose.ui.graphics.Color
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.FragmentTodayBookingBinding
import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import com.example.dormitory_app.feature_booking_wm.messages.dtos.WMsForMainRecycler
import com.example.dormitory_app.feature_booking_wm.messages.requests.TimeRangeRequest
import com.example.dormitory_app.feature_booking_wm.presentation.BookingResult
import com.example.dormitory_app.feature_booking_wm.presentation.BookingUIEvent
import com.example.dormitory_app.feature_booking_wm.presentation.BookingViewModel
import com.example.dormitory_app.feature_booking_wm.presentation.MainWMAdapter
import com.example.dormitory_app.feature_booking_wm.presentation.TimeRangeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class FragmentTodayBooking : Fragment(R.layout.fragment_today_booking) {
        private val viewModel: BookingViewModel by viewModels()
    private val adapter: MainWMAdapter by lazy { MainWMAdapter("today", clickListener) }
    private lateinit var binding: FragmentTodayBookingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerToday.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerToday.adapter = adapter
        viewModel.onEvent(BookingUIEvent.InitMain)
        setupChannelResults()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doOnClick(timeRangeDto: TimeRangeDto) {
        if (timeRangeDto.id.isNullOrBlank()) {
            //Toast.makeText(requireContext(), timeRangeDto.wmNumber.toString(), Toast.LENGTH_LONG).show()
            if (!compareTimes(timeRangeDto)) {
                setupBookingDialog(timeRangeDto)
            }
        }
        else {
            viewModel.onEvent(BookingUIEvent.OpenTimeRange(timeRangeDto.id))
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareTimes(timeRangeDto: TimeRangeDto) : Boolean {
        return parseToLocalDateTimeFromString(timeRangeDto.startTime) < LocalDateTime.now(ZoneId.of("Europe/Moscow"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseToLocalDateTimeFromString(time: String) : LocalDateTime {
        val date = getDate().split("-").map{ it.toInt()}
        val year = date[2]
        val month = date[1]
        val day = date[0]
        val timeParts = time.split(":").map {it.toInt()}
        return LocalDateTime.of(year, month, day, timeParts[0], timeParts[1])
            .atZone(ZoneId.of("Europe/Moscow"))
            .toLocalDateTime()
    }

    private val clickListener = object : TimeRangeAdapter.OnRecyclerItemClicked {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onClick(timeRangeDto: TimeRangeDto) {
            doOnClick(timeRangeDto)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupBookingDialog(timeRangeDto: TimeRangeDto) {
        val dialogBinding = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_not_booked, null)
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.show()
        val tvNotBookedWmValue: TextView = dialogBinding.findViewById(R.id.tvNotBookedWmValue)
        val tvNotBookedDateValue: TextView = dialogBinding.findViewById(R.id.tvNotBookedDateValue)
        val tvNotBookedTimeValue: TextView = dialogBinding.findViewById(R.id.tvNotBookedTimeValue)
        val switcher: SwitchCompat = dialogBinding.findViewById(R.id.switcher)
        val btnDismissDialog: Button = dialogBinding.findViewById(R.id.btnDismissDialog)
        val btnBook: Button = dialogBinding.findViewById(R.id.btnBook)
        tvNotBookedWmValue.text = timeRangeDto.wmNumber.toString()
        val date = getDate()
        tvNotBookedDateValue.text = date
        tvNotBookedTimeValue.text = "${timeRangeDto.startTime}-${timeRangeDto.endTime}"

        btnDismissDialog.setOnClickListener {
            dialog.dismiss()
        }

        btnBook.setOnClickListener {
            viewModel.onEvent(
                BookingUIEvent.BookTimeRange(
                    TimeRangeRequest(
                        timeRangeDto.wmNumber,
                        timeRangeDto.startTime,
                        timeRangeDto.endTime,
                        date,
                        switcher.isChecked
                    )
                )
            )
            dialog.dismiss()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDate() : String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.now(ZoneId.of("Europe/Moscow"))
        return date.format(formatter)
    }

    private fun setupChannelResults() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.bookingResult.collect { result->
                when(result) {
                    is BookingResult.Booking -> {
                        val dialogBinding = LayoutInflater.from(requireContext()).inflate(R.layout.booked_dialog, null)
                        val myDialog = Dialog(requireContext())
                        myDialog.setCancelable(true)
                        myDialog.setContentView(dialogBinding)
                        myDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                        myDialog.show()
                        val tvTimeValue: TextView = dialogBinding.findViewById(R.id.tvTimeValue)
                        val tvTgValue: TextView = dialogBinding.findViewById(R.id.tvTgValue)
                        val tvDrierValue: TextView = dialogBinding.findViewById(R.id.tvDrierValue)
                        tvTimeValue.text = "${result.booking.startTime.split("T")[1]}-${result.booking.endTime.split("T")[1]}"
                        tvTgValue.text = result.booking.userTg
                        tvDrierValue.text = if (result.booking.withDrier) {"Да"} else {"Нет"}
                        val btnOk: Button = dialogBinding.findViewById(R.id.btnOk)
                        btnOk.setOnClickListener {
                            myDialog.dismiss()
                        }
                    }
                    is BookingResult.BookingMainPage -> {
                        binding.loaderToday.isVisible = false
                        binding.recyclerToday.isVisible = true
                        adapter.setWms(result.info.map {
                                WMsForMainRecycler(it.wmNumber, it.timeRangesForToday)
                            }.sortedBy { it.wmNumber }
                        )
                    }
                    is BookingResult.Error -> {
                        binding.loaderToday.isVisible = false
                        binding.recyclerToday.isVisible = true
                        Toast.makeText(requireContext(), result.errorText, Toast.LENGTH_LONG).show()
                    }
                    is BookingResult.ListWM -> {}
                    BookingResult.Loading -> {
                        binding.loaderToday.isVisible = true
                        binding.recyclerToday.isVisible = false
                    }
                    is BookingResult.Success -> {
                        viewModel.onEvent(BookingUIEvent.InitMain)
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                    }
                    BookingResult.Unauthorized -> {}
                    is BookingResult.UsersBookings -> {}
                    is BookingResult.Role -> {}
                }
            }
        }
    }
}