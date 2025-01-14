package com.zackone.core.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zackone.core.data.source.local.entity.ArticleEntity
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class ArticleRoom: RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var INSTANCE: ArticleRoom? = null

        @JvmStatic
        fun getDatabase(context: Context): ArticleRoom {
            if (INSTANCE == null) {
                synchronized(ArticleRoom::class.java) {
                    val passphrase: ByteArray = SQLiteDatabase.getBytes("zackone.readnews".toCharArray())
                    val factory = SupportFactory(passphrase)

                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ArticleRoom::class.java,
                        "Article.db"
                    ).fallbackToDestructiveMigration()
                        .openHelperFactory(factory)
                        .build()
                }
            }

            return INSTANCE as ArticleRoom
        }
    }
}