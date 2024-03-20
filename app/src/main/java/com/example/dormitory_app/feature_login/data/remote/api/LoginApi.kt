package com.example.dormitory_app.feature_login.data.remote.api

import com.example.dormitory_app.feature_login.data.messages.AuthRequest
import com.example.dormitory_app.feature_login.data.messages.AuthResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {

    @POST("/auth/login")
    suspend fun singIn(@Body request: AuthRequest) : AuthResponse

    @GET("/authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )
}