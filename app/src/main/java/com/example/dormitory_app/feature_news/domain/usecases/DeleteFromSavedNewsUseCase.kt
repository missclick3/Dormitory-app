package com.example.dormitory_app.feature_news.domain.usecases

import com.example.dormitory_app.feature_news.domain.repositories.NewsRepository

class DeleteFromSavedNewsUseCase(
    val repository: NewsRepository
) {
    suspend fun execute(newsId: String) {
        repository.deleteFromSaved(newsId)
    }
}