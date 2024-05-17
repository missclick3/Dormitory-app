package com.example.dormitory_app.feature_news.messages.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.File

@Serializer(forClass = File::class)
object FileSerializer : KSerializer<File> {
    override fun serialize(encoder: Encoder, value: File) {
        encoder.encodeString(value.absolutePath)
    }

    override fun deserialize(decoder: Decoder): File {
        return File(decoder.decodeString())
    }
}