package com.example.dormitory_app.feature_news.messages.requests

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class UpdateNewsRequest(
    @SerializedName("images") val images: List<MultipartBody.Part>,
    @SerializedName("documents") val documents: List<MultipartBody.Part>
)