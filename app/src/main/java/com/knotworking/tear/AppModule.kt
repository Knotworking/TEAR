package com.knotworking.tear

import com.knotworking.data.db.TearDatabaseProvider
import com.knotworking.data.location.LocationRepositoryImpl
import com.knotworking.data.location.SharedLocationManager
import com.knotworking.data.route.LocalRouteDataSource
import com.knotworking.data.route.RouteDataSource
import com.knotworking.data.words.LocalWordDataSource
import com.knotworking.data.words.WordDataSource
import com.knotworking.data.words.WordRepositoryImpl
import com.knotworking.domain.example.GetRandomWordUseCase
import com.knotworking.domain.example.WordRepository
import com.knotworking.domain.location.GetLocationUseCase
import com.knotworking.domain.location.LocationRepository
import com.knotworking.tear.example.WordViewModel
import com.knotworking.tear.main.LocationViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ObsoleteCoroutinesApi
val presentationModule = module {
    viewModel { WordViewModel(getRandomWordUseCase = get()) }
    viewModel { LocationViewModel(getLocationUseCase = get()) }
}

val domainModule = module {
    //TODO move these to data modules
    single<WordRepository> { WordRepositoryImpl(wordDataSource = get()) }
    single<LocationRepository> { LocationRepositoryImpl(sharedLocationManager = get(), routeDataSource = get()) }

    single { GetRandomWordUseCase(wordRepository = get()) }
    single { GetLocationUseCase(locationRepository = get()) }
}

val baseDataModule = module {
    single { TearDatabaseProvider.buildDatabase(get()) }
}

val exampleDataModule = module {
    single<WordDataSource> { LocalWordDataSource() }
}

val locationDataModule = module {
    single { SharedLocationManager(context = androidContext(), externalScope = GlobalScope) }
}

val mapDataModule = module {

    // we might need to add the room lib to App-Module to allow this
//    fun provideKmMarkerDao(database: TearDatabase): KmMarkerDao {
//        return database.kmMarkerDao()
//    }

    single<RouteDataSource> { LocalRouteDataSource(database = get()) }
}