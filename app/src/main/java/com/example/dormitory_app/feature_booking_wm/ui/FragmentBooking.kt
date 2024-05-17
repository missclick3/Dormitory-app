package com.example.dormitory_app.feature_booking_wm.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.FragmentBookingBinding
import com.example.dormitory_app.feature_booking_wm.presentation.BookingResult
import com.example.dormitory_app.feature_booking_wm.presentation.BookingUIEvent
import com.example.dormitory_app.feature_booking_wm.presentation.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentBooking : Fragment(R.layout.fragment_booking){
    private val viewModel: BookingViewModel by viewModels()
    private lateinit var binding: FragmentBookingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onEvent(BookingUIEvent.GetRole)
        replaceFragment(FragmentTodayBooking())
        setupTodayButton()
        setupTomorrowButton()
        setupChannelResults()
        setupOnAdminButtonClick()
        setupOnMyBookingsButtonClick()
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.flBooking, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupOnAdminButtonClick() {
        binding.adminBtn.setOnClickListener {
            goToAdminPanel()
        }
    }

    private fun goToAdminPanel() {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.frame_layout, FragmentAdminWm())
            .addToBackStack(null)
            .commit()
    }

    private fun setupOnMyBookingsButtonClick() {
        binding.btnGoToMyBookings.setOnClickListener {
            goToMyBookings()
        }
    }

    private fun goToMyBookings() {
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.frame_layout, MyBookingsFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun setupTodayButton() {
        binding.todayButton.setOnClickListener {
            binding.todayButton.setBackgroundColor(requireContext().resources.getColor(R.color.white))
            binding.todayButton.setTextColor(requireContext().resources.getColor(R.color.blue))
            binding.tomorrowButton.setBackgroundColor(requireContext().resources.getColor(R.color.blue))
            binding.tomorrowButton.setTextColor(requireContext().resources.getColor(R.color.white))
            replaceFragment(FragmentTodayBooking())
        }
    }

    private fun setupTomorrowButton() {
        binding.tomorrowButton.setOnClickListener {
            binding.tomorrowButton.setBackgroundColor(requireContext().resources.getColor(R.color.white))
            binding.tomorrowButton.setTextColor(requireContext().resources.getColor(R.color.blue))
            binding.todayButton.setBackgroundColor(requireContext().resources.getColor(R.color.blue))
            binding.todayButton.setTextColor(requireContext().resources.getColor(R.color.white))
            replaceFragment(FragmentTomorrowBooking())
        }
    }
    private fun setupChannelResults() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.bookingResult.collect { result->
                when(result) {
                    is BookingResult.Booking -> TODO()
                    is BookingResult.BookingMainPage -> TODO()
                    is BookingResult.Error -> TODO()
                    is BookingResult.ListWM -> TODO()
                    BookingResult.Loading -> TODO()
                    is BookingResult.Role -> {
                        if (result.role == "ADMIN") binding.adminBtn.isVisible = true
                    }
                    is BookingResult.Success -> TODO()
                    BookingResult.Unauthorized -> TODO()
                    is BookingResult.UsersBookings -> TODO()
                }
            }
        }
    }

}