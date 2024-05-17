package com.example.dormitory_app.feature_news.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dormitory_app.R
import com.example.dormitory_app.databinding.FragmentNewsBinding
import com.example.dormitory_app.feature_news.domain.NewsResult
import com.example.dormitory_app.feature_news.external.messages.FileDto
import com.example.dormitory_app.feature_news.messages.dtos.NewsDtoWithFavoriteField
import com.example.dormitory_app.feature_news.util.CurrentScreen
import com.example.dormitory_app.main.FragmentListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.fragment_news) {
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var listener: FragmentListener
    private lateinit var binding: FragmentNewsBinding
    private lateinit var adapter: NewsAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement FragmentListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsBinding.bind(view)
        if (viewModel.state.userRole == "ADMIN") {
            binding.btnAddNews.visibility = View.VISIBLE
        }
        getNews()
        setupChannelResults()

    }

    private fun setupChannelResults() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.newsResult.collect { result ->
                when(result) {
                    is NewsResult.Success -> {
                        if (result.data != null) {

                            if (result.data.getNewsResponse != null) {
                                if (viewModel.state.currentScreen == CurrentScreen.SEARCH_SCREEN) {
                                    Log.println(Log.DEBUG, "WTFFFF", "I AM FROM SEARCHHHH + ${result.data.getNewsResponse.news.size}")

                                }
                                val list = result.data.getNewsResponse.news.toMutableList()
                                list.add(0, NewsDtoWithFavoriteField(
                                    id = "",
                                    title = "",
                                    category = "",
                                    content = "",
                                    images = listOf(),
                                    documents = listOf(),
                                    favorite = false
                                ))
                                adapter = NewsAdapter(viewModel)
                                adapter.submitList(list as List<Any>?)
                                binding.rvActors.adapter = adapter
                            }
                            else if (result.data.getSavedNewsResponse != null) {
                                val list = result.data.getSavedNewsResponse.news.toMutableList()
                                list.add(0, NewsDtoWithFavoriteField(
                                    id = "",
                                    title = "",
                                    category = "",
                                    content = "",
                                    images = listOf(),
                                    documents = listOf(),
                                    favorite = false
                                ))
                                adapter = NewsAdapter(viewModel)
                                adapter.submitList(list as List<Any>?)
                                binding.rvActors.adapter = adapter
                            }
                        }
                    }
                    is NewsResult.Unauthorized -> {
                        listener.navigateToLogin()
                    }
                    is NewsResult.UnknownError -> {
                        Toast.makeText(activity, "Что-то пошло не так", Toast.LENGTH_LONG).show()
                    }
                    is NewsResult.WrongFields -> {
                        Toast.makeText(activity, "Некорректное поле", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun getNews() {
        viewModel.onEvent(NewsUIEvent.GetAllNewsDesc())
    }
}