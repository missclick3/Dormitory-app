package com.example.dormitory_app.feature_news.data.messages.responses

import com.example.dormitory_app.feature_news.data.messages.dtos.NewsDtoWithFavoriteField

data class GetSavedNewsResponse(
    val news: List<NewsDtoWithFavoriteField>
)
