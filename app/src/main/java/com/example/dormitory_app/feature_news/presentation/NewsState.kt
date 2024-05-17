package com.example.dormitory_app.feature_news.presentation

import com.example.dormitory_app.feature_news.util.HeaderCategory
import com.example.dormitory_app.feature_news.util.CurrentScreen

data class NewsState(
    val userRole: String = "STUDENT",
    val searchPattern: String = "",
    val currentScreen: CurrentScreen = CurrentScreen.START_SCREEN,
    val headerCategory: HeaderCategory = HeaderCategory.ALL
)
