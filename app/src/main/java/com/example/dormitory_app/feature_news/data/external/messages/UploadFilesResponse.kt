package com.example.dormitory_app.feature_news.data.external.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadFilesResponse(
    @SerialName("file_dtos") val fileDtos: List<FileDto>
)
