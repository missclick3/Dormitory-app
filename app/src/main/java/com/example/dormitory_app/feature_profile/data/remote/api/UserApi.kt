package com.example.dormitory_app.feature_profile.data.remote.api

import com.example.dormitory_app.feature_profile.data.messages.requests.PatchUserPersonalInfoRequest
import com.example.dormitory_app.feature_profile.data.messages.responses.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserApi {
    @GET("/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ) : UserInfoResponse

    @PATCH("/user")
    suspend fun patchUser(
        @Header("Authorization") token: String,
        @Body request: PatchUserPersonalInfoRequest
    )

    @GET("/authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )
}