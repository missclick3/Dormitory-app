package com.example.dormitory_app.feature_news.data.messages.dtos

import com.example.dormitory_app.feature_news.data.external.messages.FileDto
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    val id: String,
    val title: String,
    val category: String,
    val content: String,
    val images: List<FileDto>,
    val documents: List<FileDto>
)
