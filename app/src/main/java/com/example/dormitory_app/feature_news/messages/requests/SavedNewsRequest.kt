package com.example.dormitory_app.feature_news.messages.requests

import kotlinx.serialization.Serializable

@Serializable
data class SavedNewsRequest(
    val newsId: String
)
