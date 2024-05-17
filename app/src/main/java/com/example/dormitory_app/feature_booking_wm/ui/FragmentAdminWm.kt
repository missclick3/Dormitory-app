package com.example.dormitory_app.feature_booking_wm.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.AdminBookingFragmentBinding
import com.example.dormitory_app.feature_booking_wm.messages.dtos.WashingMachineDto
import com.example.dormitory_app.feature_booking_wm.presentation.AdminWmAdapter
import com.example.dormitory_app.feature_booking_wm.presentation.BookingResult
import com.example.dormitory_app.feature_booking_wm.presentation.BookingUIEvent
import com.example.dormitory_app.feature_booking_wm.presentation.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentAdminWm : Fragment(R.layout.admin_booking_fragment) {
    private val viewModel: BookingViewModel by viewModels()
    private lateinit var binding: AdminBookingFragmentBinding
    private val adapter: AdminWmAdapter by lazy { AdminWmAdapter(deleteClickListener, changeStatusListener) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AdminBookingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerWmAdmin.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerWmAdmin.adapter = adapter
        viewModel.onEvent(BookingUIEvent.InitWms)
        setupBackButton()
        setupChanelResults()
        setupAddClick()
    }
    private fun setupAddClick() {
        binding.btnAddWM.setOnClickListener {
            openDialog()
        }
    }
    private fun openDialog() {
        val dialogBinding = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_wm, null)
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.setContentView(dialogBinding)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val wmNumberPicker: EditText = dialogBinding.findViewById(R.id.wmNumberPicker)
        val btnCloseDialog: Button = dialogBinding.findViewById(R.id.btnCloseDialog)
        val btnAdminAddWM: Button = dialogBinding.findViewById(R.id.btnAdminAddWM)

        btnCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        btnAdminAddWM.setOnClickListener {
            try {
                val num = wmNumberPicker.text.toString().toInt()
                if (num > 0) {
                    viewModel.onEvent(BookingUIEvent.AddWm(num))
                    dialog.dismiss()
                }
                else {
                    Toast.makeText(requireContext(), "Введите положительное число", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Введите число", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun setupBackButton() {
        binding.backFromAdminBooking.setOnClickListener {
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
    private fun doOnChangeStatus(washingMachineDto: WashingMachineDto) {
        AlertDialog.Builder(requireContext())
            .setTitle("Изменение статуса")
            .setMessage("Вы точно хотите изменить статус у стиральной машины № ${washingMachineDto.wmNumber}")
            .setPositiveButton("Да") {_,_, ->
                viewModel.onEvent(BookingUIEvent.ChangeWmStatus(washingMachineDto.wmNumber))
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private val changeStatusListener = object : AdminWmAdapter.OnRecyclerItemClicked {
        override fun onClick(washingMachineDto: WashingMachineDto) {
            doOnChangeStatus(washingMachineDto)
        }

    }
    private fun doOnDelete(washingMachineDto: WashingMachineDto) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление")
            .setMessage("Вы точно хотите удалить стиральную машину № ${washingMachineDto.wmNumber}")
            .setPositiveButton("Да") {_, _ ->
                viewModel.onEvent(BookingUIEvent.DeleteWm(washingMachineDto.wmNumber))
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private val deleteClickListener = object : AdminWmAdapter.OnRecyclerItemClicked {
        override fun onClick(washingMachineDto: WashingMachineDto) {
            doOnDelete(washingMachineDto)
        }
    }

    private fun setupChanelResults() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.bookingResult.collect {result ->
                when(result) {
                    is BookingResult.Booking -> {}
                    is BookingResult.BookingMainPage -> {}
                    is BookingResult.Error -> {
                        binding.loaderWms.isVisible = false
                        binding.recyclerWmAdmin.isVisible = true
                        Toast.makeText(requireContext(), result.errorText, Toast.LENGTH_LONG).show()
                    }
                    is BookingResult.ListWM -> {
                        binding.loaderWms.isVisible = false
                        binding.recyclerWmAdmin.isVisible = true
                        adapter.setWms(result.wms)
                    }
                    BookingResult.Loading -> {
                        binding.loaderWms.isVisible = true
                        binding.recyclerWmAdmin.isVisible = false
                    }
                    is BookingResult.Role -> {}
                    is BookingResult.Success -> {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                        viewModel.onEvent(BookingUIEvent.InitWms)
                    }
                    BookingResult.Unauthorized -> {}
                    is BookingResult.UsersBookings -> {}
                }
            }
        }
    }
}