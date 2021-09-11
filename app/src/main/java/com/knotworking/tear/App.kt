package com.knotworking.tear

import android.app.Application
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ObsoleteCoroutinesApi
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(viewModelModule + repositoryModule + useCaseModule + localDataModule + locationModule)
        }
    }
}