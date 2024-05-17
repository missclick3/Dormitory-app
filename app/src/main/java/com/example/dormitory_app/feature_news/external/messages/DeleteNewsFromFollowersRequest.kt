package com.example.dormitory_app.feature_news.external.messages

import kotlinx.serialization.Serializable

@Serializable
data class DeleteNewsFromFollowersRequest(
    val newsId: String
)
