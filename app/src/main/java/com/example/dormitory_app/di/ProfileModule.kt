package com.example.dormitory_app.di

import android.content.SharedPreferences
import com.example.dormitory_app.feature_login.data.remote.api.LoginApi
import com.example.dormitory_app.feature_login.data.repositories.LoginRepositoryImpl
import com.example.dormitory_app.feature_login.domain.repositories.LoginRepository
import com.example.dormitory_app.feature_profile.data.remote.api.UserApi
import com.example.dormitory_app.feature_profile.data.repositories.ProfileRepositoryImpl
import com.example.dormitory_app.feature_profile.domain.repositories.ProfileRepository
import com.example.dormitory_app.feature_profile.domain.usecases.AuthenticateUseCase
import com.example.dormitory_app.feature_profile.domain.usecases.GetUserInfoUseCase
import com.example.dormitory_app.feature_profile.domain.usecases.PatchFieldUseCase
import com.example.dormitory_app.feature_profile.domain.usecases.ProfileUseCases
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("http://192.168.240.207:8085/")
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

    @Provides
    @Singleton
    fun provideProfileUseCases(repository: ProfileRepository) : ProfileUseCases {
        return ProfileUseCases(
            getUserInfoUseCase = GetUserInfoUseCase(repository),
            patchFieldUseCase = PatchFieldUseCase(repository),
            authenticateUseCase = AuthenticateUseCase(repository)
        )
    }
}