package com.example.dormitory_app.feature_profile.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.feature_profile.data.messages.requests.PatchUserPersonalInfoRequest
import com.example.dormitory_app.feature_profile.data.messages.responses.UserInfoResponse
import com.example.dormitory_app.feature_profile.domain.ProfileResult
import com.example.dormitory_app.feature_profile.domain.usecases.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCases: ProfileUseCases
) : ViewModel(){
    var state by mutableStateOf(ProfileState())

    private val authResultChannel = Channel<AuthResult<Unit>>()
    val authResult = authResultChannel.receiveAsFlow()

    private val profileResultChannel = Channel<ProfileResult<Unit>>()
    val profileResult = profileResultChannel.receiveAsFlow()

    private val getUserChannel = Channel<ProfileResult<UserInfoResponse?>>()
    val getUserResult = getUserChannel.receiveAsFlow()
    init {
        authenticate()
    }

    fun onEvent(event: ProfileUIEvent) {
        when(event) {
            is ProfileUIEvent.EmailValueChanged -> {
                state = state.copy(emailField = event.emailValue)
            }
            is ProfileUIEvent.PhoneNumberValueChanged -> {
                state = state.copy(phoneNumberField = event.phoneNumberValue)
            }
            is ProfileUIEvent.TgValueChanged -> {
                state = state.copy(tgField = event.tgValue)
            }
            is ProfileUIEvent.ApplyChanges -> {
                val personalInfoRequest = PatchUserPersonalInfoRequest(
                    email = state.emailField,
                    phoneNumber = state.phoneNumberField,
                    tgUsername = state.tgField
                )
                changeFields(personalInfoRequest)
            }
            is ProfileUIEvent.GetUserInfo -> {
                getUserInfo()
            }
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            val result = useCases.authenticateUseCase.execute()
            authResultChannel.send(result)
        }
    }

    private fun changeFields(request: PatchUserPersonalInfoRequest) {
        viewModelScope.launch {
            val result = useCases.patchFieldUseCase.execute(request)
            profileResultChannel.send(result)
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            val result = useCases.getUserInfoUseCase.execute()
            getUserChannel.send(result)
        }
    }
}