package com.example.dormitory_app.feature_news.domain

sealed class NewsResult<T>(val data: T? = null) {
    class Success<T>(data: T? = null): NewsResult<T>(data)
    class Unauthorized<T>(data: T? = null): NewsResult<T>()
    class WrongFields<T>(data:T? = null): NewsResult<T>()
    class UnknownError<T>(data:T? = null) : NewsResult<T>()
}