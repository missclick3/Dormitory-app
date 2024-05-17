package com.example.dormitory_app.feature_news.domain.usecases

import com.example.dormitory_app.feature_news.domain.repositories.NewsRepository
import com.example.dormitory_app.feature_news.messages.requests.SavedNewsRequest

class AddToSavedNewsUseCase(
    val repository: NewsRepository
) {
    suspend fun execute(
        request: SavedNewsRequest
    ) {
        repository.addNewsToSaved(request)
    }
}