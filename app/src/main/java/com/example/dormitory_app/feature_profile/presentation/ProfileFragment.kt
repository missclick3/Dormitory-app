package com.example.dormitory_app.feature_profile.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.FragmentProfileBinding
import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.feature_login.presentation.AuthUIEvent
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
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUserInfo(userInfo: UserInfoResponse) {
        val userDTO = userInfo.userDTO
        val fluoroCertificateDTO = userInfo.fluoroCertificateDTO
        val stdsCertificateDTO = userInfo.stdsCertificateDTO
        setupStart()
        setupFIO(userDTO)
        setupCertificates(fluoroCertificateDTO, stdsCertificateDTO)
        setupContacts(userDTO)
        setupDormitory(userDTO)
        setupChangesEmail()
        setupChangesPhoneNumber()
        setupChangesTgLogin()
        setupEditButtons()
        setupOkNextClicks()
    }

    private fun setupStart() {
        binding.tvEmailInfo.isEnabled = false
        binding.tvPhoneNumberInfo.isEnabled = false
        binding.tvTgInfo.isEnabled = false
        binding.editEmail.setImageResource(R.drawable.baseline_edit_24)
        binding.editPhoneNumber.setImageResource(R.drawable.baseline_edit_24)
        binding.editTg.setImageResource(R.drawable.baseline_edit_24)
    }
    private fun setupOkNextClicks() {
        okNextClickEmail()
        okNextClickPhoneNumber()
        okNextClickTgLogin()
    }

    private fun okNextClickTgLogin() {
        binding.tvTgInfo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.editTg.setImageResource(R.drawable.baseline_edit_24)
                binding.tvTgInfo.isEnabled = false
                viewModel.onEvent(ProfileUIEvent.ApplyChanges)
                true
            } else {
                false
            }
        }
    }
    private fun okNextClickPhoneNumber() {
        binding.tvPhoneNumberInfo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.editPhoneNumber.setImageResource(R.drawable.baseline_edit_24)
                binding.tvPhoneNumberInfo.isEnabled = false
                viewModel.onEvent(ProfileUIEvent.ApplyChanges)
                true
            } else {
                false
            }
        }
    }
    private fun okNextClickEmail() {
        binding.tvEmailInfo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.editEmail.setImageResource(R.drawable.baseline_edit_24)
                binding.tvEmailInfo.isEnabled = false
                viewModel.onEvent(ProfileUIEvent.ApplyChanges)
                true
            } else {
                false
            }
        }
    }
    private fun setupEditButtons() {
        setupEditEmailButton()
        setupEditPhoneNumberButton()
        setupEditTgLoginButton()
    }

    private fun setupEditTgLoginButton() {
        binding.editTg.setOnClickListener {
            if (!binding.tvTgInfo.isEnabled) {
                if (binding.tvEmailInfo.isEnabled) {
                    binding.editEmail.setImageResource(R.drawable.baseline_edit_24)
                }
                if (binding.tvPhoneNumberInfo.isEnabled) {
                    binding.editPhoneNumber.setImageResource(R.drawable.baseline_edit_24)
                }
                binding.editTg.setImageResource(R.drawable.baseline_check_24)
                binding.tvTgInfo.isEnabled = true
                binding.tvTgInfo.requestFocus()
                binding.tvTgInfo.setSelection(binding.tvTgInfo.text.length)
                WindowCompat.getInsetsController(requireActivity().window, binding.tvTgInfo).show(WindowInsetsCompat.Type.ime())
            }
            else {
                binding.editTg.setImageResource(R.drawable.baseline_edit_24)
                binding.tvTgInfo.isEnabled = false
                viewModel.onEvent(ProfileUIEvent.ApplyChanges)
            }
        }
    }
    private fun setupEditPhoneNumberButton() {
        binding.editPhoneNumber.setOnClickListener {
            if (!binding.tvPhoneNumberInfo.isEnabled) {
                if (binding.tvEmailInfo.isEnabled) {
                    binding.editEmail.setImageResource(R.drawable.baseline_edit_24)
                }
                if (binding.tvTgInfo.isEnabled) {
                    binding.editTg.setImageResource(R.drawable.baseline_edit_24)
                }
                binding.editPhoneNumber.setImageResource(R.drawable.baseline_check_24)
                binding.tvPhoneNumberInfo.isEnabled = true
                binding.tvPhoneNumberInfo.requestFocus()
                binding.tvPhoneNumberInfo.setSelection(binding.tvPhoneNumberInfo.text.length)
                WindowCompat.getInsetsController(requireActivity().window, binding.tvPhoneNumberInfo).show(WindowInsetsCompat.Type.ime())
            }
            else {
                binding.editPhoneNumber.setImageResource(R.drawable.baseline_edit_24)
                binding.tvPhoneNumberInfo.isEnabled = false
                viewModel.onEvent(ProfileUIEvent.ApplyChanges)
            }
        }
    }
    private fun setupEditEmailButton() {
        binding.editEmail.setOnClickListener {
            if (!binding.tvEmailInfo.isEnabled) {
                if (binding.tvPhoneNumberInfo.isEnabled) {
                    binding.editPhoneNumber.setImageResource(R.drawable.baseline_edit_24)
                }
                if (binding.tvTgInfo.isEnabled) {
                    binding.editTg.setImageResource(R.drawable.baseline_edit_24)
                }
                binding.editEmail.setImageResource(R.drawable.baseline_check_24)
                binding.tvEmailInfo.isEnabled = true
                binding.tvEmailInfo.requestFocus()
                binding.tvEmailInfo.setSelection(binding.tvEmailInfo.text.length)
                WindowCompat.getInsetsController(requireActivity().window, binding.tvEmailInfo).show(WindowInsetsCompat.Type.ime())
            }
            else {
                binding.editEmail.setImageResource(R.drawable.baseline_edit_24)
                binding.tvEmailInfo.isEnabled = false
                viewModel.onEvent(ProfileUIEvent.ApplyChanges)
            }
        }
    }
    private fun setupChangesTgLogin() {
        binding.tvTgInfo.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    viewModel.onEvent(ProfileUIEvent.TgValueChanged(s.toString()))
                    if (!isTgLoginValid(s.toString())) {
                        binding.tvTgStatus.visibility = View.VISIBLE
                    } else {
                        binding.tvTgStatus.visibility = View.GONE
                    }
                }
            }
        )
    }
    private fun isTgLoginValid(str: String) : Boolean {
        return Regex("^@[a-zA-Z0-9_]{5,32}$").matches(str) || str.isBlank()
    }
    private fun setupChangesPhoneNumber() {
        binding.tvPhoneNumberInfo.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    viewModel.onEvent(ProfileUIEvent.PhoneNumberValueChanged(s.toString()))
                    if (!isPhoneNumber(s.toString())) {
                        binding.tvPhoneNumberStatus.visibility = View.VISIBLE
                    } else {
                        binding.tvPhoneNumberStatus.visibility = View.GONE
                    }
                }
            }
        )
    }

    private fun isPhoneNumber(str: String) : Boolean {
        return Regex("^\\+7\\d{10}$").matches(str) || str.isBlank()
    }
    private fun setupChangesEmail() {
        binding.tvEmailInfo.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    viewModel.onEvent(ProfileUIEvent.EmailValueChanged(s.toString()))
                    if (!isEmail(s.toString())) {
                        binding.tvEmailStatus.visibility = View.VISIBLE
                    } else {
                        binding.tvEmailStatus.visibility = View.GONE
                    }
                }
            }
        )
    }
    private fun isEmail(str: String) : Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches() || str.isBlank()
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
                            viewModel.state = viewModel.state.copy(
                                emailField = userInfo.userDTO.email,
                                phoneNumberField = userInfo.userDTO.phoneNumber,
                                tgField = userInfo.userDTO.tgUsername,
                            )
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
                        getUser()
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