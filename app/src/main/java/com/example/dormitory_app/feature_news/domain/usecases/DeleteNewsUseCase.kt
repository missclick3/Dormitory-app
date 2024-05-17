package com.example.dormitory_app.feature_news.domain.usecases

import com.example.dormitory_app.feature_news.domain.NewsResult
import com.example.dormitory_app.feature_news.domain.repositories.NewsRepository
import com.example.dormitory_app.feature_news.util.WrappedResponse

class DeleteNewsUseCase(
    val repository: NewsRepository
) {
    suspend fun execute(newsId: String) : NewsResult<WrappedResponse?> {
        return repository.deleteNews(newsId)
    }
}