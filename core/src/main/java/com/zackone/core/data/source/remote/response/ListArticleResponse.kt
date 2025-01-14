package com.zackone.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListArticleResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("articles")
    val articles: List<ArticleResponse>
)
