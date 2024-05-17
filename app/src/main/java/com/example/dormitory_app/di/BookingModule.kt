package com.example.dormitory_app.di

import android.content.SharedPreferences
import com.example.dormitory_app.feature_booking_wm.data.BookingApi
import com.example.dormitory_app.feature_booking_wm.data.BookingWMRepositoryImpl
import com.example.dormitory_app.feature_booking_wm.domain.BookingWMRepository
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
object BookingModule {
    @Provides
    @Singleton
    fun provideBookingApi() : BookingApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("http://192.168.26.207:8070/")
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideBookingRepository(
        api: BookingApi,
        sharedPreferences: SharedPreferences
    ) : BookingWMRepository {
        return BookingWMRepositoryImpl(sharedPreferences, api)
    }
}