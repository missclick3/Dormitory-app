package com.example.dormitory_app.feature_profile.domain.usecases

import com.example.dormitory_app.feature_profile.data.messages.requests.PatchUserPersonalInfoRequest
import com.example.dormitory_app.feature_profile.domain.ProfileResult
import com.example.dormitory_app.feature_profile.domain.repositories.ProfileRepository

class PatchFieldUseCase(
    private val repository: ProfileRepository
) {
    suspend fun execute(request: PatchUserPersonalInfoRequest): ProfileResult<Unit> {
        return repository.patchField(request)
    }
}