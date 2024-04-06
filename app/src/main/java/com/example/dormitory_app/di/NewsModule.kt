package com.example.dormitory_app.di

import com.example.dormitory_app.feature_login.data.remote.api.LoginApi
import com.example.dormitory_app.feature_news.data.remote.NewsApi
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
object NewsModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("http://192.168.3.207:8090/")
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType))
            .build()
            .create()
    }
}