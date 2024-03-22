package com.example.dormitory_app.feature_login.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.dormitory_app.feature_login.data.remote.api.LoginApi
import com.example.dormitory_app.feature_login.data.repositories.LoginRepositoryImpl
import com.example.dormitory_app.feature_login.domain.repositories.LoginRepository
import com.example.dormitory_app.feature_login.domain.usecases.AuthUseCases
import com.example.dormitory_app.feature_login.domain.usecases.AuthenticateUseCase
import com.example.dormitory_app.feature_login.domain.usecases.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideLoginApi(): LoginApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.192.1:8085/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
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