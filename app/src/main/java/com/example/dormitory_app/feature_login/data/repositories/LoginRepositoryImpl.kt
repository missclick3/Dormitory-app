package com.example.dormitory_app.feature_login.data.repositories

import android.content.SharedPreferences
import com.example.dormitory_app.feature_login.data.messages.AuthRequest
import com.example.dormitory_app.feature_login.data.remote.api.LoginApi
import com.example.dormitory_app.feature_login.domain.AuthResult
import com.example.dormitory_app.feature_login.domain.repositories.LoginRepository
import retrofit2.HttpException

class LoginRepositoryImpl (
    private val loginApi: LoginApi,
    private val prefs: SharedPreferences
) : LoginRepository {
    override suspend fun signIn(request: AuthRequest): AuthResult<Unit> {
        return try {
            val response = loginApi.singIn(request)
            prefs.edit()
                .putString("jwt", response.token)
                .apply()
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401){
                AuthResult.Unauthorized()
            }
            else if (e.code() == 409) {
                AuthResult.WrongFields()
            }
            else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            loginApi.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401){
                AuthResult.Unauthorized()
            }
            else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}