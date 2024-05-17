package com.example.dormitory_app.feature_booking_wm.ui

import android.app.Dialog
import android.graphics.Color
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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.FragmentTomorrowBookingBinding
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
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class FragmentTomorrowBooking : Fragment(R.layout.fragment_tomorrow_booking) {
    private val viewModel: BookingViewModel by viewModels()
    private val adapter: MainWMAdapter by lazy { MainWMAdapter("tomorrow", clickListener) }
    private lateinit var binding: FragmentTomorrowBookingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTomorrowBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerTomorrow.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTomorrow.adapter = adapter
        viewModel.onEvent(BookingUIEvent.InitMain)
        setupChannelResults()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doOnClick(timeRangeDto: TimeRangeDto) {
        if (timeRangeDto.id.isNullOrBlank()) {
            setupBookingDialog(timeRangeDto)
        }
        else {
            viewModel.onEvent(BookingUIEvent.OpenTimeRange(timeRangeDto.id))
        }
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
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
        val date = LocalDate.now(ZoneId.of("Europe/Moscow")).plusDays(1)
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
                        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
                        binding.loaderTomorrow.isVisible = false
                        binding.recyclerTomorrow.isVisible = true
                        adapter.setWms(result.info.map {
                            WMsForMainRecycler(it.wmNumber, it.timeRangesForTomorrow)
                        }.sortedBy { it.wmNumber }
                        )
                    }
                    is BookingResult.Error -> {
                        binding.loaderTomorrow.isVisible = false
                        binding.recyclerTomorrow.isVisible = true
                        Toast.makeText(requireContext(), result.errorText, Toast.LENGTH_LONG).show()
                    }
                    is BookingResult.ListWM -> {}
                    BookingResult.Loading -> {
                        binding.loaderTomorrow.isVisible = true
                        binding.recyclerTomorrow.isVisible = false
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