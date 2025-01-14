package com.zackone.readnews.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
class FavoriteViewModel(
    private val articleUseCase: ArticleUseCase
): ViewModel() {
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    private val _allArticles = MutableLiveData<List<Article>>()
    private val allArticles: LiveData<List<Article>> = _allArticles

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

    fun fetchFavoriteArticles() {
        articleUseCase.getFavoriteArticles()
            .asLiveData()
            .observeForever { result ->
            _articles.value = result
            _allArticles.value = result
        }
    }

    fun searchFavoriteArticles(query: String) {
        articleUseCase.searchFavoriteArticles(query)
            .asLiveData()
            .observeForever { result ->
            _articles.value = result
        }
    }
}