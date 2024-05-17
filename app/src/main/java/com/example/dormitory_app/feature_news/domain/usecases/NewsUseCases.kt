package com.example.dormitory_app.feature_news.domain.usecases

data class NewsUseCases(
    val addToSavedNewsUseCase: AddToSavedNewsUseCase,
    val deleteFromSavedNewsUseCase: DeleteFromSavedNewsUseCase,
    val deleteNewsUseCase: DeleteNewsUseCase,
    val getAllNewsAscUseCase: GetAllNewsAscUseCase,
    val getAllNewsDescUseCase: GetAllNewsDescUseCase,
    val getArticlesAscUseCase: GetArticlesAscUseCase,
    val getArticlesDescUseCase: GetArticlesDescUseCase,
    val getOrdersAscUseCase: GetOrdersAscUseCase,
    val getOrdersDescUseCase: GetOrdersDescUseCase,
    val getSavedNewsUseCase: GetSavedNewsUseCase,
    val authenticateUseCase: AuthenticateUseCase
)
