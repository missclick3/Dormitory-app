package com.example.dormitory_app.feature_profile.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.FragmentProfileBinding
import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.feature_login.presentation.LoginActivity
import com.example.dormitory_app.feature_profile.data.messages.dtos.CertificateDTO
import com.example.dormitory_app.feature_profile.data.messages.dtos.UserDTO
import com.example.dormitory_app.feature_profile.data.messages.responses.UserInfoResponse
import com.example.dormitory_app.feature_profile.domain.ProfileResult
import com.example.dormitory_app.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var listener: ProfileFragmentListener
    private lateinit var binding: FragmentProfileBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProfileFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ProfileFragmentListener")
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        setupSwipe()
        setupChannelResults()
        getUser()
        binding.logout.setOnClickListener {
            showLogoutConformationDialog()
        }
    }
    private fun setupSwipe() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            getUser()
        }
    }
    private fun showLogoutConformationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Выход из аккаунта")
            .setMessage("Вы уверены, что хотите выйти из аккаунта?")
            .setPositiveButton("Да") {_, _ ->
                viewModel.onEvent(ProfileUIEvent.Logout)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
    private fun setupFIO(userDTO: UserDTO) {
        val fioString = userDTO.surname + " " + userDTO.name + " " + userDTO.patronymic
        binding.tvFIOInfo.text = fioString
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCertificateText(certificateDTO: CertificateDTO?) : String {
        return if (certificateDTO == null) {
            "Сертификат не добавлен"
        } else {
            val daysLeft = daysOfCertificateLeft(certificateDTO.expireDate)
            val r = abs(daysLeft) % 10
            if (daysLeft < 0) {
                if (r in listOf(2L, 3L, 4L)) {
                    "Сертификат просрочен уже ${-daysLeft} дня"
                } else if (r == 1L) {
                    "Сертификат просрочен уже ${-daysLeft} день"
                }
                else {
                    "Сертификат просрочен уже ${-daysLeft} дней"
                }
            }
            else {
                if (r in listOf(2L, 3L, 4L)) {
                    "Осталось $daysLeft дня"
                }
                else if (r == 1L) {
                    "Осталось $daysLeft день"
                }
                else {
                    "Осталось $daysLeft дней"
                }

            }
        }
    }
    private fun setupDormitory(userDTO: UserDTO) {
        binding.tvDormitoryInfo.text = userDTO.address
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCertificateTextColor(certificateDTO: CertificateDTO?) : Int {
        if (certificateDTO == null || daysOfCertificateLeft(certificateDTO.expireDate) < 0 ) {
            return android.graphics.Color.RED
        }
        return android.graphics.Color.GREEN
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCertificates(fluoroCertificateDTO: CertificateDTO?, stdsCertificateDTO: CertificateDTO?) {
        binding.tvFluCertInfo.text = setupCertificateText(fluoroCertificateDTO)
        binding.tvFluCertInfo.setTextColor(setupCertificateTextColor(fluoroCertificateDTO))
        binding.tvSTDsCertInfo.text = setupCertificateText(stdsCertificateDTO)
        binding.tvSTDsCertInfo.setTextColor(setupCertificateTextColor(stdsCertificateDTO))
    }
    private fun setupContacts(userDTO: UserDTO) {
        binding.tvEmailInfo.setText(userDTO.email)
        binding.tvPhoneNumberInfo.setText(userDTO.phoneNumber)
        binding.tvTgInfo.setText(userDTO.tgUsername)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUserInfo(userInfo: UserInfoResponse) {
        val userDTO = userInfo.userDTO
        val fluoroCertificateDTO = userInfo.fluoroCertificateDTO
        val stdsCertificateDTO = userInfo.stdsCertificateDTO
        setupFIO(userDTO)
        setupCertificates(fluoroCertificateDTO, stdsCertificateDTO)
        setupContacts(userDTO)
        setupDormitory(userDTO)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupChannelResults() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.profileResult.collect { result ->
                when (result) {
                    is ProfileResult.Success -> {

                        if (result.data == null) {
                            Log.println(Log.DEBUG, "WTFFFFFFFFFF", "AUTHENTICATED")
                        }
                        if (result.data != null) {
                            val userInfo = result.data
                            setupUserInfo(userInfo)
//                            Toast.makeText(activity, "Здарова ${userInfo.userDTO.name}", Toast.LENGTH_SHORT).show()
                            Log.println(Log.DEBUG, "WTFFFFFFFFFF", " ${userInfo.userDTO.name}")
                        }
                    }

                    is ProfileResult.Unauthorized -> {
                        Log.println(Log.DEBUG, "WTFFFFFFFFFF", "I DROPED HERE")
                        listener.navigateToLogin()

                    }
                    is ProfileResult.UnknownError -> {
                        Toast.makeText(activity, "Что-то пошло не так", Toast.LENGTH_LONG).show()
                    }
                    is ProfileResult.WrongFields -> {
                        Toast.makeText(activity, "Некорректное поле", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysOfCertificateLeft(date: String) : Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val expireDate = LocalDate.parse(date, formatter)
        return ChronoUnit.DAYS.between(LocalDate.now(), expireDate)
    }
    private fun getUser() {
        viewModel.onEvent(ProfileUIEvent.GetUserInfo)
        binding.swipeRefreshLayout.isRefreshing = false
    }
}