package com.zackone.core

import androidx.room.Room
import com.zackone.core.data.ArticleRepository
import com.zackone.core.data.source.local.LocalDataSource
import com.zackone.core.data.source.remote.network.ApiConfig
import com.zackone.core.data.source.local.room.ArticleRoom
import com.zackone.core.data.source.remote.RemoteDataSource
import com.zackone.core.domain.repository.IArticleRepository
import com.zackone.core.domain.usecase.ArticleInteractor
import com.zackone.core.domain.usecase.ArticleUseCase
import com.zackone.core.util.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        ArticleRoom.getDatabase(get())
    }

    single {
        get<ArticleRoom>().articleDao()
    }
}

val networkModule = module {
    single {
        ApiConfig.getApiService(get())
    }
}

val repositoryModule = module {
    single {
        LocalDataSource.getInstance(get())
    }

    single {
        RemoteDataSource.getInstance(get())
    }

    single {
        AppExecutors()
    }

    single<IArticleRepository> {
        ArticleRepository.getInstance(get(), get())
    }
}

val useCaseModule = module {
    single<ArticleUseCase> {
        ArticleInteractor(get())
    }
}