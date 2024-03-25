package com.example.dormitory_app.di

import android.content.SharedPreferences
import com.example.dormitory_app.feature_login.data.remote.api.LoginApi
import com.example.dormitory_app.feature_login.data.repositories.LoginRepositoryImpl
import com.example.dormitory_app.feature_login.domain.repositories.LoginRepository
import com.example.dormitory_app.feature_profile.data.remote.api.UserApi
import com.example.dormitory_app.feature_profile.data.repositories.ProfileRepositoryImpl
import com.example.dormitory_app.feature_profile.domain.repositories.ProfileRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn
object ProfileModule {
    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("http://192.168.187.207:8085/")
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideProfileRepository(userApi: UserApi, prefs: SharedPreferences): ProfileRepository {
        return ProfileRepositoryImpl(userApi, prefs)
    }
}