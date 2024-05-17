package com.example.dormitory_app.di

import android.content.SharedPreferences
import com.example.dormitory_app.feature_login.data.remote.api.LoginApi
import com.example.dormitory_app.feature_login.data.repositories.LoginRepositoryImpl
import com.example.dormitory_app.feature_login.domain.repositories.LoginRepository
import com.example.dormitory_app.feature_login.domain.usecases.AuthUseCases
import com.example.dormitory_app.feature_login.domain.usecases.AuthenticateUseCase
import com.example.dormitory_app.feature_login.domain.usecases.LoginUseCase
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
object AuthModule {
    @Provides
    @Singleton
    fun provideLoginApi(): LoginApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("http:192.168.26.207:8070/")
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType))
            .build()
            .create()
    }
    @Provides
    @Singleton
    fun provideLoginRepository(loginApi: LoginApi, prefs: SharedPreferences): LoginRepository {
        return LoginRepositoryImpl(loginApi, prefs)
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(repository: LoginRepository) : AuthUseCases{
        return AuthUseCases(
            loginUseCase = LoginUseCase(repository),
            authenticateUseCase = AuthenticateUseCase(repository)
        )
    }
}