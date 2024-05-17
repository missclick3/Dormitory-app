package com.example.dormitory_app.feature_news.messages.responses

import com.example.dormitory_app.feature_news.messages.dtos.NewsDtoWithFavoriteField
import kotlinx.serialization.Serializable


@Serializable
data class GetNewsResponse(
    val news: List<NewsDtoWithFavoriteField>
)
