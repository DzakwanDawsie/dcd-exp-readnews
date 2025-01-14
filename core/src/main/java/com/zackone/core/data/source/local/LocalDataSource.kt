package com.zackone.core.data.source.local

import com.zackone.core.data.source.local.entity.ArticleEntity
import com.zackone.core.data.source.local.room.ArticleDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource private constructor(private val articleDao: ArticleDao) {
    companion object {
        private var instance: LocalDataSource? = null

        fun getInstance(articleDao: ArticleDao): LocalDataSource =
            instance ?: synchronized(this) {
                instance ?: LocalDataSource(articleDao)
            }
    }

    fun getArticles(): Flow<List<ArticleEntity>> = articleDao.getArticles()

    fun getArticles(query: String): Flow<List<ArticleEntity>> = articleDao.getArticles(query)

    fun getFavoriteArticles(): Flow<List<ArticleEntity>> = articleDao.getFavoriteArticles()

    fun searchFavoriteArticles(query: String): Flow<List<ArticleEntity>> = articleDao.searchFavoriteArticles(query)

    suspend fun insert(articles: List<ArticleEntity>) = articleDao.insert(articles)

    suspend fun setFavorite(article: ArticleEntity, isFavorite: Boolean) {
        article.isFavorite = isFavorite
        articleDao.update(article)
    }
}