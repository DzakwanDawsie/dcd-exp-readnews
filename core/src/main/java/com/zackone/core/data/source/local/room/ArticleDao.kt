package com.zackone.core.data.source.local.room
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zackone.core.data.source.local.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE title like '%' || :query || '%' ORDER BY publishedAt DESC")
    fun getArticles(query: String): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: List<ArticleEntity>)

    @Update
    suspend fun update(article: ArticleEntity)

    @Query("SELECT * FROM articles WHERE isFavorite = 1 ORDER BY publishedAt DESC")
    fun getFavoriteArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE isFavorite = 1 and title like '%' || :query || '%' ORDER BY publishedAt ASC")
    fun searchFavoriteArticles(query: String): Flow<List<ArticleEntity>>
}