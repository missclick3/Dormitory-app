package com.example.dormitory_app.feature_profile.domain

sealed class ProfileResult<T>(val data: T? = null) {
    class Success<T>(data: T? = null): ProfileResult<T>(data)
    class Unauthorized<T>(data: T? = null): ProfileResult<T>()
    class WrongFields<T>(data: T? = null): ProfileResult<T>()
    class UnknownError<T>(data: T? = null): ProfileResult<T>()
}