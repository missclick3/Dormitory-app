package com.example.dormitory_app.di

import android.content.SharedPreferences
import com.example.dormitory_app.feature_login.data.remote.api.LoginApi
import com.example.dormitory_app.feature_news.data.remote.NewsApi
import com.example.dormitory_app.feature_news.data.remote.SavedNewsApi
import com.example.dormitory_app.feature_news.data.repositories.NewsRepositoryImpl
import com.example.dormitory_app.feature_news.domain.repositories.NewsRepository
import com.example.dormitory_app.feature_news.domain.usecases.AddToSavedNewsUseCase
import com.example.dormitory_app.feature_news.domain.usecases.AuthenticateUseCase
import com.example.dormitory_app.feature_news.domain.usecases.DeleteFromSavedNewsUseCase
import com.example.dormitory_app.feature_news.domain.usecases.DeleteNewsUseCase
import com.example.dormitory_app.feature_news.domain.usecases.GetAllNewsAscUseCase
import com.example.dormitory_app.feature_news.domain.usecases.GetAllNewsDescUseCase
import com.example.dormitory_app.feature_news.domain.usecases.GetArticlesAscUseCase
import com.example.dormitory_app.feature_news.domain.usecases.GetArticlesDescUseCase
import com.example.dormitory_app.feature_news.domain.usecases.GetOrdersAscUseCase
import com.example.dormitory_app.feature_news.domain.usecases.GetOrdersDescUseCase
import com.example.dormitory_app.feature_news.domain.usecases.GetSavedNewsUseCase
import com.example.dormitory_app.feature_news.domain.usecases.NewsUseCases
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
            .baseUrl("http://192.168.26.207:8070/")
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideSavedNewsApi(): SavedNewsApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("http://192.168.42.207:8085/")
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory(contentType))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideNewsRepository(newsApi: NewsApi, savedNewsApi: SavedNewsApi,sharedPreferences: SharedPreferences) : NewsRepository {
        return NewsRepositoryImpl(newsApi, savedNewsApi, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideNewsUseCases(repository: NewsRepository) : NewsUseCases {
        return NewsUseCases(
            addToSavedNewsUseCase = AddToSavedNewsUseCase(repository),
            deleteFromSavedNewsUseCase = DeleteFromSavedNewsUseCase(repository),
            deleteNewsUseCase = DeleteNewsUseCase(repository),
            getAllNewsAscUseCase = GetAllNewsAscUseCase(repository),
            getAllNewsDescUseCase = GetAllNewsDescUseCase(repository),
            getArticlesAscUseCase = GetArticlesAscUseCase(repository),
            getArticlesDescUseCase = GetArticlesDescUseCase(repository),
            getOrdersAscUseCase = GetOrdersAscUseCase(repository),
            getOrdersDescUseCase = GetOrdersDescUseCase(repository),
            getSavedNewsUseCase = GetSavedNewsUseCase(repository),
            authenticateUseCase = AuthenticateUseCase(repository)
        )
    }
}