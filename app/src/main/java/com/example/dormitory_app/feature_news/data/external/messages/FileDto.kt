package com.example.dormitory_app.feature_news.data.external.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileDto(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_name") val fileName: String,
    val url: String
)
