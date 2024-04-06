package com.example.dormitory_app.feature_news.data.messages.requests

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import okhttp3.MultipartBody
import retrofit2.http.Multipart


data class  CreateNewsRequest(
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String,
    @SerializedName("content") val content: String,
    @SerializedName("images") val images: List<MultipartBody.Part>,
    @SerializedName("documents") val documents: List<MultipartBody.Part>
)
