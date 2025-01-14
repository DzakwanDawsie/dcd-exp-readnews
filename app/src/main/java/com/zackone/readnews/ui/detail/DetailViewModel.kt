package com.zackone.readnews.ui.detail

import androidx.lifecycle.ViewModel
import com.zackone.core.domain.model.Article
import com.zackone.core.domain.usecase.ArticleUseCase

class DetailViewModel(
    private val articleUseCase: ArticleUseCase
): ViewModel() {
    fun setFavorite(article: Article, isFavorite: Boolean) {
        articleUseCase.setFavorite(article, isFavorite)
    }
}