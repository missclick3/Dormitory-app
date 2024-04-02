package com.example.dormitory_app.feature_profile.domain.usecases

import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.feature_profile.data.messages.responses.UserInfoResponse
import com.example.dormitory_app.feature_profile.domain.ProfileResult
import com.example.dormitory_app.feature_profile.domain.repositories.ProfileRepository

class AuthenticateUseCase(
    private val repository: ProfileRepository
) {
    suspend fun execute() : ProfileResult<UserInfoResponse?> {
        return repository.authenticate()
    }
}