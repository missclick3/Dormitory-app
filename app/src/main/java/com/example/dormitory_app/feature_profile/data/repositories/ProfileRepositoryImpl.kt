package com.example.dormitory_app.feature_profile.data.repositories

import android.content.SharedPreferences
import android.util.Log
import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.feature_profile.data.messages.requests.PatchUserPersonalInfoRequest
import com.example.dormitory_app.feature_profile.data.messages.responses.UserInfoResponse
import com.example.dormitory_app.feature_profile.data.remote.api.UserApi
import com.example.dormitory_app.feature_profile.domain.ProfileResult
import com.example.dormitory_app.feature_profile.domain.repositories.ProfileRepository
import retrofit2.HttpException

class ProfileRepositoryImpl(
    private val userApi: UserApi,
    private val prefs: SharedPreferences
) : ProfileRepository{
    override suspend fun getUser(): ProfileResult<UserInfoResponse?> {
        return try {
            val token = prefs.getString("jwt", null) ?: return ProfileResult.Unauthorized()
            val response = userApi.getUser("Bearer $token")
            ProfileResult.Success(response)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                ProfileResult.Unauthorized()
            }
            else {
                ProfileResult.UnknownError()
            }
        } catch (e: Exception) {
            ProfileResult.UnknownError()
        }
    }

    override suspend fun patchField(request: PatchUserPersonalInfoRequest): ProfileResult<UserInfoResponse?> {
        return try {
            userApi.patchUser(request)
            ProfileResult.Success(null)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                ProfileResult.Unauthorized()
            }
            else if (e.code() == 409) {
                ProfileResult.WrongFields()
            }
            else {
                ProfileResult.UnknownError()
            }
        } catch (e: Exception) {
            ProfileResult.UnknownError()
        }
    }

    override suspend fun authenticate(): ProfileResult<UserInfoResponse?> {
        return try {
            val token = prefs.getString("jwt", null) ?: return ProfileResult.Unauthorized()
            userApi.authenticate("Bearer $token")
            ProfileResult.Success()
        } catch (e: HttpException) {
            if (e.code() == 401){
                Log.println(Log.DEBUG, "WTFFFFFFFF", "I am HERE")
                ProfileResult.Unauthorized()
            }
            else {
                ProfileResult.UnknownError()
            }
        } catch (e: Exception) {
            ProfileResult.UnknownError()
        }
    }

    override suspend fun logout() : ProfileResult<UserInfoResponse?> {
        return try {
            val token = prefs.getString("jwt", null) ?: return ProfileResult.Unauthorized()
            prefs.edit().remove("jwt").commit()
            ProfileResult.Success()
        } catch (e: Exception) {
            ProfileResult.UnknownError()
        }
    }

}