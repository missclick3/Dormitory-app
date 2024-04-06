package com.example.dormitory_app.feature_news.data.messages.util

import kotlinx.serialization.Serializable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Serializable
data class SerializableMultipart(
    val name: String, // Теперь используем 'name' здесь
    val filename: String,
    val contentType: String,
    @Serializable(with = FileSerializer::class)
    val file: File
) {
    fun toMultipartBodyPart(): MultipartBody.Part {
        val requestBody = file.asRequestBody(contentType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, filename, requestBody)
    }

    companion object {
        fun fromMultipartBodyPart(part: MultipartBody.Part): SerializableMultipart {
            val contentDisposition = part.headers?.get("Content-Disposition") ?: ""
            val nameStartIndex = contentDisposition.indexOf("name=")
            val nameEndIndex = contentDisposition.indexOf(";", startIndex = nameStartIndex)
            val name = contentDisposition.substring(nameStartIndex + 5, if (nameEndIndex != -1) nameEndIndex else contentDisposition.length).trim('"')

            return SerializableMultipart(
                name,
                part.headers?.get("Content-Disposition")!!,
                part.body!!.contentType().toString(),
                File(part.body!!.javaClass.getDeclaredField("file").apply { isAccessible = true }.get(part.body) as String)
            )
        }
    }
}