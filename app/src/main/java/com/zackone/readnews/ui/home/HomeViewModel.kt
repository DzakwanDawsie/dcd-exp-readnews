package com.zackone.readnews.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zackone.core.data.Resource
import com.zackone.core.domain.model.Article
import com.zackone.core.domain.usecase.ArticleUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val articleUseCase: ArticleUseCase
): ViewModel() {
    private val apiKey = "1c44a5ea723f4c2aa35f08e586ccb34f"

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    private val _allArticles = MutableLiveData<List<Article>>()
    private val allArticles: LiveData<List<Article>> = _allArticles

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    val queryChannel = MutableStateFlow("")
    val searchResult = queryChannel
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            allArticles.value?.filter { article -> article.title.lowercase().contains(it.lowercase()) }
        }
        .asLiveData()

    fun fetchArticles() {
        articleUseCase.getArticles(null, apiKey)
            .asLiveData()
            .observeForever { article ->
                when (article) {
                    is Resource.Loading -> _loading.value = true
                    is Resource.Success -> {
                        _loading.value = false
                        _allArticles.value = article.data ?: listOf()
                        _articles.value = article.data ?: listOf()
                    }
                    is Resource.Error -> {
                        _loading.value = false
                        _error.value = article.message.toString()
                    }
                }
            }
    }

    fun searchArticles(query: String) {
        articleUseCase.getArticles(query, apiKey)
            .asLiveData()
            .observeForever { article ->
                when (article) {
                    is Resource.Loading -> _loading.value = true
                    is Resource.Success -> {
                        _loading.value = false
                        _articles.value = article.data ?: listOf()
                    }
                    is Resource.Error -> {
                        _loading.value = false
                        _error.value = article.message.toString()
                    }
                }
            }
    }
}