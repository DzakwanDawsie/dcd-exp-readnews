package com.zackone.readnews.favorite

import com.zackone.core.data.ArticleRepository
import com.zackone.core.data.source.local.LocalDataSource
import com.zackone.core.data.source.local.room.ArticleRoom
import com.zackone.core.data.source.remote.RemoteDataSource
import com.zackone.core.data.source.remote.network.ApiConfig
import com.zackone.readnews.ui.detail.DetailViewModel
import com.zackone.core.util.AppExecutors
import com.zackone.readnews.favorite.ui.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
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

    single {
        ArticleRepository.getInstance(get(), get())
    }
}

val viewModelModule = module {
    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        DetailViewModel(get())
    }
}