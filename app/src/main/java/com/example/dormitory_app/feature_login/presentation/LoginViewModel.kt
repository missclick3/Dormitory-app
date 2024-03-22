package com.example.dormitory_app.feature_login.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dormitory_app.feature_login.data.repositories.LoginRepositoryImpl
import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.feature_login.domain.repositories.LoginRepository
import com.example.dormitory_app.feature_login.domain.usecases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCases: AuthUseCases
) : ViewModel(){

    var state by mutableStateOf(AuthState())
    private val resultChanel = Channel<AuthResult<Unit>>()
    val authResults = resultChanel.receiveAsFlow()

    fun onEvent(event: AuthUIEvent) {
        when(event) {
            is AuthUIEvent.SignInUsernameChanged -> {
                state = state.copy(singInUsername = event.value)
            }
            is AuthUIEvent.SignInPasswordChanged -> {
                state = state.copy(signInPassword = event.value)
            }
            is AuthUIEvent.SignIn -> {
                signIn()
            }
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = useCases.loginUseCase.execute(
                state.singInUsername,
                state.signInPassword
            )
            resultChanel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = useCases.authenticateUseCase.execute()
            resultChanel.send(result)
            state = state.copy(isLoading = false)
        }
    }
}