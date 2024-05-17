package com.example.dormitory_app.feature_news.data.repositories

import android.content.SharedPreferences
import android.util.Log
import com.example.dormitory_app.feature_news.messages.dtos.NewsDto
import com.example.dormitory_app.feature_news.messages.requests.CreateNewsRequest
import com.example.dormitory_app.feature_news.messages.requests.UpdateNewsRequest
import com.example.dormitory_app.feature_news.messages.responses.GetNewsResponse
import com.example.dormitory_app.feature_news.messages.responses.GetSavedNewsResponse
import com.example.dormitory_app.feature_news.messages.util.NewsCategory
import com.example.dormitory_app.feature_news.messages.util.SortType
import com.example.dormitory_app.feature_news.data.remote.NewsApi
import com.example.dormitory_app.feature_news.data.remote.SavedNewsApi
import com.example.dormitory_app.feature_news.domain.NewsResult
import com.example.dormitory_app.feature_news.domain.repositories.NewsRepository
import com.example.dormitory_app.feature_news.messages.requests.SavedNewsRequest
import com.example.dormitory_app.feature_news.util.WrappedResponse
import retrofit2.HttpException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val savedNewsApi: SavedNewsApi,
    private val prefs: SharedPreferences
) : NewsRepository{
    override suspend fun getNews(
        newsCategory: NewsCategory?,
        searchPattern: String?,
        sortType: SortType
    ): NewsResult<WrappedResponse?> {
        return try {
            val token = prefs.getString("jwt", null) ?: return NewsResult.Unauthorized()
            val response = newsApi.getNews(
                searchPattern = searchPattern,
                token = "Bearer $token",
                newsCategory = newsCategory,
                sortType = sortType
            )
            NewsResult.Success(
                WrappedResponse(getNewsResponse = response)
            )
        } catch (e: HttpException) {
            if (e.code() == 401) {
                NewsResult.Unauthorized()
            } else if (e.code() == 409) {
                NewsResult.WrongFields()
            }
            else {
                NewsResult.UnknownError()
            }
        } catch (e: Exception) {
            NewsResult.UnknownError()
        }
    }

    override suspend fun getSavedNews(): NewsResult<WrappedResponse?> {
        return try {
            val token = prefs.getString("jwt", null) ?: return NewsResult.Unauthorized()
            val response = newsApi.getSavedNews("Bearer $token")
            NewsResult.Success(
                WrappedResponse(getSavedNewsResponse = response)
            )
        } catch (e: HttpException) {
            if (e.code() == 401) {
                NewsResult.Unauthorized()
            } else if (e.code() == 409) {
                NewsResult.WrongFields()
            }
            else {
                NewsResult.UnknownError()
            }
        } catch (e: Exception) {
            NewsResult.UnknownError()
        }
    }

    override suspend fun authenticate(): NewsResult<WrappedResponse?> {
        return try {
            val token = prefs.getString("jwt", null) ?: return NewsResult.Unauthorized()
            val response = newsApi.authenticate("Bearer $token")
            Log.println(Log.DEBUG, "AUTH", response.role)
            NewsResult.Success(
                WrappedResponse(stringResponse = response.role)
            )
        } catch (e: HttpException) {
            Log.println(Log.DEBUG, "AUTH", e.code().toString())
            if (e.code() == 401) {
                NewsResult.Unauthorized()
            } else if (e.code() == 409) {
                NewsResult.WrongFields()
            }
            else {
                NewsResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.println(Log.DEBUG, "AUTH", e.toString())
            NewsResult.UnknownError()
        }
    }

    override suspend fun putNews(
        newsId: String,
        createNewsRequest: CreateNewsRequest
    ): NewsResult<WrappedResponse?> {
        return try {
            val token = prefs.getString("jwt", null) ?: return NewsResult.Unauthorized()
            val response = newsApi.putNews(
                newsId,
                createNewsRequest,
                "Bearer $token"
            )
            NewsResult.Success(
                WrappedResponse(newsDto = response)
            )
        } catch (e: HttpException) {
            if (e.code() == 401) {
                NewsResult.Unauthorized()
            } else if (e.code() == 409) {
                NewsResult.WrongFields()
            }
            else {
                NewsResult.UnknownError()
            }
        } catch (e: Exception) {
            NewsResult.UnknownError()
        }
    }

    override suspend fun deleteNews(newsId: String): NewsResult<WrappedResponse?> {
        return try {
            val token = prefs.getString("jwt", null) ?: return NewsResult.Unauthorized()
            val response = newsApi.deleteNews(
                newsId,
                "Bearer $token"
            )
            NewsResult.Success(
                WrappedResponse(unitResponse = response)
            )
        } catch (e: HttpException) {
            if (e.code() == 401) {
                NewsResult.Unauthorized()
            } else if (e.code() == 409) {
                NewsResult.WrongFields()
            }
            else {
                NewsResult.UnknownError()
            }
        } catch (e: Exception) {
            NewsResult.UnknownError()
        }
    }

    override suspend fun updateNews(
        newsId: String,
        title: String?,
        category: String?,
        content: String?,
        updateNewsRequest: UpdateNewsRequest
    ): NewsResult<WrappedResponse?> {
        return try {
            val token = prefs.getString("jwt", null) ?: return NewsResult.Unauthorized()
            val response = newsApi.updateNews(
                newsId,
                title,
                category,
                content,
                updateNewsRequest,
                "Bearer $token"
            )
            NewsResult.Success(
                WrappedResponse(newsDto = response)
            )
        } catch (e: HttpException) {
            if (e.code() == 401) {
                NewsResult.Unauthorized()
            } else if (e.code() == 409) {
                NewsResult.WrongFields()
            }
            else {
                NewsResult.UnknownError()
            }
        } catch (e: Exception) {
            NewsResult.UnknownError()
        }
    }

    override suspend fun createNews(createNewsRequest: CreateNewsRequest): NewsResult<WrappedResponse?> {
        return try {
            val token = prefs.getString("jwt", null) ?: return NewsResult.Unauthorized()
            val response = newsApi.createNews(
                "Bearer $token",
                createNewsRequest
            )
            NewsResult.Success(
                WrappedResponse(newsDto = response)
            )
        } catch (e: HttpException) {
            if (e.code() == 401) {
                NewsResult.Unauthorized()
            } else if (e.code() == 409) {
                NewsResult.WrongFields()
            }
            else {
                NewsResult.UnknownError()
            }
        } catch (e: Exception) {
            NewsResult.UnknownError()
        }
    }

    override suspend fun addNewsToSaved(request: SavedNewsRequest) {
        val token = prefs.getString("jwt", null)
        savedNewsApi.addNewsToSaved(
            "Bearer $token",
            request
        )
    }

    override suspend fun deleteFromSaved(newsId: String) {
        val token = prefs.getString("jwt", null)
        savedNewsApi.deleteFromSaved(
            "Bearer $token",
            newsId
        )
    }

}