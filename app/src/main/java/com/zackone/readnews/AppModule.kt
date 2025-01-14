package com.zackone.readnews

import com.zackone.readnews.ui.detail.DetailViewModel
import com.zackone.readnews.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(get())
    }

    viewModel {
        DetailViewModel(get())
    }
}