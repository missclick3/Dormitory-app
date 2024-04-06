package com.example.dormitory_app.feature_news.data.messages.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExceptionResponse(
    @SerialName("error_message") val errorMessage: String
)
