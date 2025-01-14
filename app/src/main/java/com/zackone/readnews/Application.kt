package com.zackone.readnews

import android.app.Application
import com.zackone.core.databaseModule
import com.zackone.core.networkModule
import com.zackone.core.repositoryModule
import com.zackone.core.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(listOf(
                databaseModule,
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            ))
        }
    }
}