package com.zackone.core.util

import com.zackone.core.data.source.local.entity.ArticleEntity
import com.zackone.core.data.source.remote.response.ArticleResponse
import com.zackone.core.domain.model.Article

object DataMapper {
    fun mapResponsesToEntities(input: List<ArticleResponse>): List<ArticleEntity> =
        input.map {
            ArticleEntity(
                url = it.url,
                author = it.author,
                title = it.title,
                description = it.description,
                urlToImage = it.urlToImage,
                publishedAt = it.publishedAt,
                isFavorite = false
            )
        }

    fun mapEntitiesToDomain(input: List<ArticleEntity>): List<Article> =
        input.map {
            Article(
                url = it.url,
                author = it.author,
                title = it.title,
                description = it.description,
                urlToImage = it.urlToImage,
                publishedAt = it.publishedAt,
                isFavorite = it.isFavorite,
            )
        }

    fun mapDomainToEntity(input: Article) = ArticleEntity(
        url = input.url,
        author = input.author,
        title = input.title,
        description = input.description,
        urlToImage = input.urlToImage,
        publishedAt = input.publishedAt,
        isFavorite = input.isFavorite,
    )
}
