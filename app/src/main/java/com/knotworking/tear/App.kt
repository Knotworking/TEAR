package com.knotworking.tear

import android.app.Application
import com.knotworking.data.*
import com.knotworking.domain.domainModule
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ObsoleteCoroutinesApi
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                presentationModule +
                        domainModule +
                        baseDataModule +
                        exampleDataModule +
                        locationDataModule +
                        routeDataModule +
                        wordpressApiModule
            )
        }
    }
}