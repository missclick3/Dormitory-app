package com.example.dormitory_app.feature_login.domain

sealed class AuthResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null): AuthResult<T>(data)
    class Unauthorized<T>: AuthResult<T>()
    class WrongFields<T>: AuthResult<T>()
    class UnknownError<T>: AuthResult<T>()
}