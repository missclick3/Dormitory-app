package com.example.dormitory_app.feature_news.presentation

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dormitory_app.feature_news.domain.NewsResult
import com.example.dormitory_app.feature_news.domain.usecases.NewsUseCases
import com.example.dormitory_app.feature_news.messages.requests.SavedNewsRequest
import com.example.dormitory_app.feature_news.util.WrappedResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val useCases: NewsUseCases,
    private val sharedPrefs: SharedPreferences
) : ViewModel(){
    var state by mutableStateOf(NewsState())
    private val resultChannel = Channel<NewsResult<WrappedResponse?>>()
    val newsResult = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }
    fun onEvent(event: NewsUIEvent) {
        when(event) {
            is NewsUIEvent.AddToSaveNews -> {
                addToSaved(event.request)
            }
            is NewsUIEvent.DeleteFromSaved -> {
                deleteFromSaved(event.newsId)
            }
            is NewsUIEvent.DeleteNews -> {
                deleteNews(event.newsId)
            }
            is NewsUIEvent.GetAllNewsAsc -> {
                getAllNewsAsc(event.searchPattern)
            }
            is NewsUIEvent.GetAllNewsDesc -> {
                getAllNewsDesc(event.searchPattern)
            }
            is NewsUIEvent.GetArticlesAsc -> {
                getArticlesAsc(event.searchPattern)
            }
            is NewsUIEvent.GetArticlesDesc -> {
                getArticlesDesc(event.searchPattern)
            }
            is NewsUIEvent.GetOrdersAsc -> {
                getOrdersAsc(event.searchPattern)
            }
            is NewsUIEvent.GetOrdersDesc -> {
                getOrdersDesc(event.searchPattern)
            }
            is NewsUIEvent.GetSavedNews -> {
                getSavedNews()
            }

            is NewsUIEvent.SearchPatternChanged -> {
                state = state.copy(searchPattern = event.searchPattern)
            }

            is NewsUIEvent.CurrentScreenChanged -> {
                state = state.copy(currentScreen = event.currentScreen)
            }
            is NewsUIEvent.HeaderCategoryChanged -> {
                state = state.copy(headerCategory = event.headerCategory)
            }
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            val result = useCases.authenticateUseCase.execute()
            state = state.copy(userRole = result.data?.stringResponse.toString())
            Log.println(Log.DEBUG, "Auth1", result.data?.stringResponse.toString())
            resultChannel.send(result)
        }
    }
    private fun deleteFromSaved(id: String) {
        viewModelScope.launch {
            useCases.deleteFromSavedNewsUseCase.execute(id)
        }
    }

    private fun addToSaved(savedNewsRequest: SavedNewsRequest) {
        viewModelScope.launch {
            useCases.addToSavedNewsUseCase.execute(savedNewsRequest)
        }
    }
    private fun deleteNews(id: String) {
        viewModelScope.launch {
            val result = useCases.deleteNewsUseCase.execute(id)
            resultChannel.send(result)
        }
    }
    private fun getAllNewsAsc(searchPattern: String? = null) {
        viewModelScope.launch {
            val result = useCases.getAllNewsAscUseCase.execute(searchPattern)
            resultChannel.send(result)
        }
    }

    private fun getAllNewsDesc(searchPattern: String? = null) {
        Log.println(Log.DEBUG, "WTFFF", "SEARCH_PATTERN: ${searchPattern.toString()}")
        viewModelScope.launch{
            val result = useCases.getAllNewsDescUseCase.execute(searchPattern)
            resultChannel.send(result)
        }
    }

    private fun getArticlesAsc(searchPattern: String? = null) {
        viewModelScope.launch {
            val result = useCases.getArticlesAscUseCase.execute(searchPattern)
            resultChannel.send(result)
        }
    }

    private fun getArticlesDesc(searchPattern: String? = null) {
        viewModelScope.launch {
            val result = useCases.getArticlesDescUseCase.execute(searchPattern)
            resultChannel.send(result)
        }
    }

    private fun getOrdersAsc(searchPattern: String? = null) {
        viewModelScope.launch {
            val result = useCases.getOrdersAscUseCase.execute(searchPattern)
            resultChannel.send(result)
        }
    }

    private fun getOrdersDesc(searchPattern: String? = null) {
        viewModelScope.launch {
            val result = useCases.getOrdersDescUseCase.execute(searchPattern)
            resultChannel.send(result)
        }
    }

    private fun getSavedNews() {
        viewModelScope.launch {
            val result = useCases.getSavedNewsUseCase.execute()
            resultChannel.send(result)
        }
    }
}