package com.example.dormitory_app.feature_news.data.external.messages

import kotlinx.serialization.Serializable

@Serializable
data class DeleteFilesRequest(
    val ids: List<String>
)
