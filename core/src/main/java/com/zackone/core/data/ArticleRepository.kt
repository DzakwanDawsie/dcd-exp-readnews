package com.zackone.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.zackone.core.data.source.local.LocalDataSource
import com.zackone.core.data.source.remote.RemoteDataSource
import com.zackone.core.data.source.remote.network.ApiResponse
import com.zackone.core.data.source.remote.response.ArticleResponse
import com.zackone.core.domain.model.Article
import com.zackone.core.domain.repository.IArticleRepository
import com.zackone.core.util.AppExecutors
import com.zackone.core.util.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ArticleRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
): IArticleRepository {
    companion object {
        @Volatile
        private var instance: ArticleRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
        ): ArticleRepository =
            instance ?: synchronized(this) {
                instance ?: ArticleRepository(remoteData, localData)
            }
    }

    override fun getArticles(query: String?, apiKey: String): Flow<Resource<List<Article>>> =
        object : NetworkBoundResource<List<Article>, List<ArticleResponse>>() {
            override fun loadFromDB(): Flow<List<Article>> {
                return when {
                    query.isNullOrEmpty() -> {
                        localDataSource.getArticles().map { DataMapper.mapEntitiesToDomain(it) }
                    }

                    else -> {
                        localDataSource.getArticles(query).map { DataMapper.mapEntitiesToDomain(it) }
                    }
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ArticleResponse>>> =
                remoteDataSource.getArticles(query, apiKey)

            override suspend fun saveCallResult(data: List<ArticleResponse>) {
                val articles = DataMapper.mapResponsesToEntities(data)
                localDataSource.insert(articles)
            }

            override fun shouldFetch(data: List<Article>?): Boolean =
                data.isNullOrEmpty()
        }.asFlow()

    override fun getFavoriteArticles(): Flow<List<Article>> {
        return localDataSource.getFavoriteArticles().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun searchFavoriteArticles(query: String): Flow<List<Article>> {
        return localDataSource.searchFavoriteArticles(query).map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFavorite(article: Article, isFavorite: Boolean) {
        val articleEntity = DataMapper.mapDomainToEntity(article)
        MainScope().launch {
            localDataSource.setFavorite(articleEntity, isFavorite)
        }
    }
}