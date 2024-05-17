package com.example.dormitory_app.feature_news.presentation

import com.example.dormitory_app.feature_news.messages.requests.SavedNewsRequest
import com.example.dormitory_app.feature_news.util.CurrentScreen
import com.example.dormitory_app.feature_news.util.HeaderCategory

sealed class NewsUIEvent {
    data class AddToSaveNews(val request: SavedNewsRequest) : NewsUIEvent()
    data class DeleteFromSaved(val newsId: String) : NewsUIEvent()
    data class GetAllNewsDesc(val searchPattern: String? = null): NewsUIEvent()
    data class GetAllNewsAsc(val searchPattern: String? = null): NewsUIEvent()
    data class GetArticlesAsc(val searchPattern: String? = null): NewsUIEvent()
    data class GetArticlesDesc(val searchPattern: String? = null) : NewsUIEvent()
    data class GetOrdersAsc(val searchPattern: String? = null) : NewsUIEvent()
    data class GetOrdersDesc(val searchPattern: String? = null) : NewsUIEvent()
    data class DeleteNews(val newsId: String) : NewsUIEvent()
    data object GetSavedNews : NewsUIEvent()
    data class SearchPatternChanged(val searchPattern: String = "") : NewsUIEvent()
    data class CurrentScreenChanged(val currentScreen: CurrentScreen) : NewsUIEvent()
    data class HeaderCategoryChanged(val headerCategory: HeaderCategory) : NewsUIEvent()
}