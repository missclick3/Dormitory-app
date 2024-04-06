package com.example.dormitory_app.feature_news.data.remote

import com.example.dormitory_app.feature_news.data.messages.dtos.NewsDto
import com.example.dormitory_app.feature_news.data.messages.requests.CreateNewsRequest
import com.example.dormitory_app.feature_news.data.messages.requests.UpdateNewsRequest
import com.example.dormitory_app.feature_news.data.messages.responses.GetNewsResponse
import com.example.dormitory_app.feature_news.data.messages.responses.GetSavedNewsResponse
import com.example.dormitory_app.feature_news.data.messages.util.NewsCategory
import com.example.dormitory_app.feature_news.data.messages.util.SortType
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {
    @GET("/news")
    suspend fun getNews(
        @Header("Authorization") token: String,
        @Query("category") newsCategory: NewsCategory? = null,
        @Query("search_pattern") searchPattern: String? = null,
        @Query("sort_type") sortType: SortType
    ) : GetNewsResponse

    @GET("/news/favorites")
    suspend fun getSavedNews(
        @Header("Authorization") token: String
    ) : GetSavedNewsResponse

    @GET("/ping-user-app")
    suspend fun authenticate(
        @Header("Authorization") token: String
    ) : String

    @PUT("admin/news/{news_id}")
    suspend fun putNews(
        @Path("news_id") newsId: String,
        @Body createNewsRequest: CreateNewsRequest,
        @Header("Authorization") authorizationHeader: String
    ): NewsDto

    @DELETE("admin/news/{news_id}")
    suspend fun deleteNews(
        @Path("news_id") newsId: String,
        @Header("Authorization") authorizationHeader: String
    )

    @PATCH("news/{news_id}")
    suspend fun updateNews(
        @Path("news_id") newsId: String,
        @Query("title") title: String?,
        @Query("category") category: String?,
        @Query("content") content: String?,
        @Body updateNewsRequest: UpdateNewsRequest,
        @Header("Authorization") authorizationHeader: String
    ): NewsDto



    @POST("/admin/news")
    suspend fun createNews(
        @Header("Authorization") token: String,
        createNewsRequest: CreateNewsRequest
    ) : NewsDto
}