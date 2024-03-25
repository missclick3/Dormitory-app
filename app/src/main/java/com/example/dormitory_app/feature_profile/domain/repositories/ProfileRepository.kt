package com.example.dormitory_app.feature_profile.domain.repositories

import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.feature_profile.data.messages.requests.PatchUserPersonalInfoRequest
import com.example.dormitory_app.feature_profile.data.messages.responses.UserInfoResponse
import com.example.dormitory_app.feature_profile.domain.ProfileResult

interface ProfileRepository {
    suspend fun getUser(): ProfileResult<UserInfoResponse?>
    suspend fun patchField(request: PatchUserPersonalInfoRequest): ProfileResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}