package com.example.dormitory_app.feature_news.data.messages.responses

import com.example.dormitory_app.feature_news.data.messages.dtos.NewsDtoWithFavoriteField

data class GetNewsResponse(
    val news: List<NewsDtoWithFavoriteField>
)
