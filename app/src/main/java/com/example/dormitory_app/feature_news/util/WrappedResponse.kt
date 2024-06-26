package com.example.dormitory_app.feature_news.util

import com.example.dormitory_app.feature_news.messages.dtos.NewsDto
import com.example.dormitory_app.feature_news.messages.responses.GetNewsResponse
import com.example.dormitory_app.feature_news.messages.responses.GetSavedNewsResponse

data class WrappedResponse(
    val unitResponse: Unit? = null,
    val getNewsResponse: GetNewsResponse? = null,
    val getSavedNewsResponse: GetSavedNewsResponse? = null,
    val newsDto: NewsDto? = null,
    val stringResponse: String? = null
)
