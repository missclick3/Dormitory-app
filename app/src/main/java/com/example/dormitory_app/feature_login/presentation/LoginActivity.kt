package com.example.dormitory_app.feature_login.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.ActivityLoginBinding
import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private val _binding :ActivityLoginBinding? = null
    private val binding: ActivityLoginBinding?
        get() = _binding
    val state = viewModel.state

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding?.password?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(AuthUIEvent.SignInPasswordChanged(s.toString()))
            }
        })
        binding?.username?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(AuthUIEvent.SignInUsernameChanged(s.toString()))
            }
        })

        binding?.loginButton?.setOnClickListener {
            val username = binding?.username?.text.toString()
            val password = binding?.password?.text.toString()
            viewModel.onEvent(AuthUIEvent.SignIn)
        }
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.authResults.collect { result ->
                when(result) {
                    is AuthResult.Authorized -> {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is AuthResult.Unauthorized -> Toast.makeText(this@LoginActivity, "Вы не авторизованы", Toast.LENGTH_LONG).show()
                    is AuthResult.UnknownError -> Toast.makeText(this@LoginActivity, "Unknown error", Toast.LENGTH_LONG).show()
                    is AuthResult.WrongFields -> {
                        binding?.password?.text = null
                        Toast.makeText(this@LoginActivity, "Неправильный логин или пароль", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}