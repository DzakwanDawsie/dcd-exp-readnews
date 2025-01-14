package com.zackone.core.data.source.remote

import android.util.Log
import com.zackone.core.data.source.remote.network.ApiResponse
import com.zackone.core.data.source.remote.network.ApiService
import com.zackone.core.data.source.remote.response.ArticleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class RemoteDataSource private constructor(private val apiService: ApiService) {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(service)
            }
    }

    suspend fun getArticles(query: String?, apiKey: String): Flow<ApiResponse<List<ArticleResponse>>> {
        return flow {
            try {
                val response = apiService.getArticles(query, apiKey = apiKey)
                val articles = response.articles

                if (articles.isNotEmpty()) {
                    emit(ApiResponse.Success(articles))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (exception: HttpException) {
                emit(ApiResponse.Error(exception.toString()))
                Log.e("RemoteDataSource", exception.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}