package com.example.dormitory_app.feature_news.domain.usecases

import android.util.Log
import com.example.dormitory_app.feature_news.domain.NewsResult
import com.example.dormitory_app.feature_news.domain.repositories.NewsRepository
import com.example.dormitory_app.feature_news.messages.util.SortType
import com.example.dormitory_app.feature_news.util.WrappedResponse

class GetAllNewsAscUseCase(
    val repository: NewsRepository
) {
    suspend fun execute(
        searchPattern: String? = null
    ): NewsResult<WrappedResponse?> {
        Log.println(Log.DEBUG, "USECASE", searchPattern!!)
        return repository.getNews(
            searchPattern = searchPattern,
            sortType = SortType.ASCENDING
        )
    }
}