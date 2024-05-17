package com.example.dormitory_app.feature_news.data.remote

import com.example.dormitory_app.feature_news.messages.requests.SavedNewsRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SavedNewsApi {
    @POST("/saved-news")
    suspend fun addNewsToSaved(
        @Header("Authorization") token: String,
        @Body request: SavedNewsRequest
    )

    @DELETE("/saved-news/{id}")
    suspend fun deleteFromSaved(
        @Header("Authorization") token: String,
        @Path("id") newsId: String
    )
}