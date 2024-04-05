package com.example.dormitory_app.feature_login.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.authResults.collect { result ->
                when(result) {
                    is AuthResult.Authorized -> {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    is AuthResult.Unauthorized -> {
                        configureLayout()
                        Toast.makeText(this@LoginActivity, "Вы не авторизованы", Toast.LENGTH_LONG).show()
                    }
                    is AuthResult.UnknownError -> {
                        configureLayout()
                        Toast.makeText(this@LoginActivity, "Unknown error", Toast.LENGTH_LONG).show()
                    }
                    is AuthResult.WrongFields -> {
                        configureLayout()
                        Toast.makeText(this@LoginActivity, "Неправильный логин или пароль", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun configureLayout() {
        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(AuthUIEvent.SignInPasswordChanged(s.toString()))
            }
        })
        binding.username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.onEvent(AuthUIEvent.SignInUsernameChanged(s.toString()))
            }
        })

        binding.loginButton.setOnClickListener {
            viewModel.onEvent(AuthUIEvent.SignIn)
        }
    }
}