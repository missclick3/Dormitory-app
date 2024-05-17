package com.example.dormitory_app.feature_booking_wm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.FragmentMyBookingsBinding
import com.example.dormitory_app.feature_booking_wm.messages.dtos.TimeRangeDto
import com.example.dormitory_app.feature_booking_wm.presentation.BookingResult
import com.example.dormitory_app.feature_booking_wm.presentation.BookingUIEvent
import com.example.dormitory_app.feature_booking_wm.presentation.BookingViewModel
import com.example.dormitory_app.feature_booking_wm.presentation.MyBookingsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyBookingsFragment : Fragment(R.layout.fragment_my_bookings) {
    private val viewModel: BookingViewModel by viewModels()
    private val todayAdapter: MyBookingsAdapter by lazy { MyBookingsAdapter(deleteListener) }
    private val tomorrowAdapter: MyBookingsAdapter by lazy { MyBookingsAdapter(deleteListener) }
    private lateinit var binding: FragmentMyBookingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerMyTodayBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMyTodayBookings.adapter = todayAdapter

        binding.recyclerMyBookingsTomorrow.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMyBookingsTomorrow.adapter = tomorrowAdapter
        viewModel.onEvent(BookingUIEvent.InitTimeRanges)
        setupBackButton()
        setupChannelResults()
    }

    private fun setupChannelResults() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.bookingResult.collect {result->
                when(result) {
                    is BookingResult.Booking -> {}
                    is BookingResult.BookingMainPage -> {}
                    is BookingResult.Error -> {
                        binding.loaderMyBookings.isVisible = false
                        binding.recyclerMyTodayBookings.isVisible = true
                        binding.recyclerMyBookingsTomorrow.isVisible = true
                        binding.myBookingsTodayTitle.isVisible = true
                        binding.myBookingsTomorrowTitle.isVisible = true
                    }
                    is BookingResult.ListWM -> {}
                    BookingResult.Loading -> {
                        binding.loaderMyBookings.isVisible = true
                        binding.recyclerMyTodayBookings.isVisible = false
                        binding.recyclerMyBookingsTomorrow.isVisible = false
                        binding.myBookingsTodayTitle.isVisible = false
                        binding.myBookingsTomorrowTitle.isVisible = false
                    }
                    is BookingResult.Role -> {}
                    is BookingResult.Success -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                        viewModel.onEvent(BookingUIEvent.InitTimeRanges)
                    }
                    BookingResult.Unauthorized -> {}
                    is BookingResult.UsersBookings -> {
                        binding.loaderMyBookings.isVisible = false
                        binding.recyclerMyTodayBookings.isVisible = true
                        binding.recyclerMyBookingsTomorrow.isVisible = true
                        binding.myBookingsTodayTitle.isVisible = true
                        binding.myBookingsTomorrowTitle.isVisible = true

                        todayAdapter.setBookings(result.bookings.todayBookings.sortedBy { it.startTime })
                        tomorrowAdapter.setBookings(result.bookings.tomorrowBookings.sortedBy { it.startTime })
                    }
                }
            }
        }
    }

    private fun setupBackButton() {
        binding.backFromMyBookings.setOnClickListener {
            backToFragmentBooking()
        }
    }

    private fun backToFragmentBooking() {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.frame_layout, FragmentBooking())
            .addToBackStack(null)
            .commit()
    }

    private fun doOnDelete(timeRangeDto: TimeRangeDto) {
        AlertDialog.Builder(requireContext())
            .setTitle("Отмена брони")
            .setMessage("Вы действительно хотите отменить бронь")
            .setPositiveButton("Да") {_, _ ->
                viewModel.onEvent(BookingUIEvent.DeleteBooking(timeRangeDto.id!!))
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    private val deleteListener = object : MyBookingsAdapter.OnRecyclerItemClicked {
        override fun onClick(timeRangeDto: TimeRangeDto) {
            doOnDelete(timeRangeDto)
        }

    }
}