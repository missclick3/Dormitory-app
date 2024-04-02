package com.example.dormitory_app.feature_profile.domain.usecases

import com.example.dormitory_app.feature_profile.data.messages.responses.UserInfoResponse
import com.example.dormitory_app.feature_profile.domain.ProfileResult
import com.example.dormitory_app.feature_profile.domain.repositories.ProfileRepository

class LogoutUseCase(val repository: ProfileRepository) {
    suspend fun execute() : ProfileResult<UserInfoResponse?>{
        return repository.logout()
    }
}