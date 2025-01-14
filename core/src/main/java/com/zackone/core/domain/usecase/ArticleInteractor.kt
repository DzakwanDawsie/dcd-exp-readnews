package com.zackone.core.domain.usecase

import com.zackone.core.domain.model.Article
import com.zackone.core.domain.repository.IArticleRepository
import kotlinx.coroutines.flow.Flow

class ArticleInteractor(private val articleRepository: IArticleRepository): ArticleUseCase {
    override fun getArticles(query: String?, apiKey: String) =
        articleRepository.getArticles(query, apiKey)

    override fun getFavoriteArticles() =
        articleRepository.getFavoriteArticles()

    override fun searchFavoriteArticles(query: String): Flow<List<Article>> =
        articleRepository.searchFavoriteArticles(query)

    override fun setFavorite(article: Article, isFavorite: Boolean) =
        articleRepository.setFavorite(article, isFavorite)
}