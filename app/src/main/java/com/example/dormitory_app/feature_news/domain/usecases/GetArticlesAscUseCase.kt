package com.example.dormitory_app.feature_news.domain.usecases

import com.example.dormitory_app.feature_news.domain.NewsResult
import com.example.dormitory_app.feature_news.domain.repositories.NewsRepository
import com.example.dormitory_app.feature_news.messages.util.NewsCategory
import com.example.dormitory_app.feature_news.messages.util.SortType
import com.example.dormitory_app.feature_news.util.WrappedResponse

class GetArticlesAscUseCase(
    val repository: NewsRepository
) {
    suspend fun execute(
        searchPattern: String? = null
    ) : NewsResult<WrappedResponse?> {
        return repository.getNews(
            searchPattern = searchPattern,
            newsCategory = NewsCategory.NEWS,
            sortType = SortType.ASCENDING
        )
    }
}