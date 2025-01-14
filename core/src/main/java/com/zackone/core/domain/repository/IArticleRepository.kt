package com.zackone.core.domain.repository

import androidx.lifecycle.LiveData
import com.zackone.core.data.Resource
import com.zackone.core.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface IArticleRepository {
    fun getArticles(query: String?, apiKey: String): Flow<Resource<List<Article>>>

    fun getFavoriteArticles(): Flow<List<Article>>

    fun searchFavoriteArticles(query: String): Flow<List<Article>>

    fun setFavorite(article: Article, isFavorite: Boolean)
}