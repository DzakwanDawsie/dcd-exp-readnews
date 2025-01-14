package com.zackone.core.data.source.remote.network

import com.zackone.core.data.source.remote.response.ListArticleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getArticles(
        @Query("q") query: String?,
        @Query("language") language: String? = "en",
        @Query("apiKey") apiKey: String,
    ): ListArticleResponse

}