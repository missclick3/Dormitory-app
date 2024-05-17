package com.example.dormitory_app.feature_news.domain.repositories

import com.example.dormitory_app.feature_news.messages.dtos.NewsDto
import com.example.dormitory_app.feature_news.messages.requests.CreateNewsRequest
import com.example.dormitory_app.feature_news.messages.requests.UpdateNewsRequest
import com.example.dormitory_app.feature_news.messages.responses.GetNewsResponse
import com.example.dormitory_app.feature_news.messages.responses.GetSavedNewsResponse
import com.example.dormitory_app.feature_news.messages.util.NewsCategory
import com.example.dormitory_app.feature_news.messages.util.SortType
import com.example.dormitory_app.feature_news.domain.NewsResult
import com.example.dormitory_app.feature_news.messages.requests.SavedNewsRequest
import com.example.dormitory_app.feature_news.util.WrappedResponse
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsRepository {
    suspend fun getNews(
        newsCategory: NewsCategory? = null,
        searchPattern: String? = null,
        sortType: SortType = SortType.DESCENDING
    ) : NewsResult<WrappedResponse?>
    suspend fun getSavedNews() : NewsResult<WrappedResponse?>
    suspend fun authenticate() : NewsResult<WrappedResponse?>
    suspend fun putNews(
        newsId: String,
        createNewsRequest: CreateNewsRequest
    ): NewsResult<WrappedResponse?>
    suspend fun deleteNews(newsId: String) : NewsResult<WrappedResponse?>
    suspend fun updateNews(
        newsId: String,
        title: String?,
        category: String?,
        content: String?,
        updateNewsRequest: UpdateNewsRequest
    ) : NewsResult<WrappedResponse?>
    suspend fun createNews(createNewsRequest: CreateNewsRequest) : NewsResult<WrappedResponse?>

    suspend fun addNewsToSaved(request: SavedNewsRequest)
    suspend fun deleteFromSaved(newsId: String)
}