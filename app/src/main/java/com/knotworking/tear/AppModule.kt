package com.knotworking.tear

import com.knotworking.data.location.LocationRepositoryImpl
import com.knotworking.data.location.SharedLocationManager
import com.knotworking.data.map.InMemoryMapDataSource
import com.knotworking.data.map.LocalMapDataSource
import com.knotworking.data.map.MapDataSource
import com.knotworking.data.map.MapRepositoryImpl
import com.knotworking.data.words.LocalWordDataSource
import com.knotworking.data.words.WordDataSource
import com.knotworking.data.words.WordRepositoryImpl
import com.knotworking.domain.example.GetRandomWordUseCase
import com.knotworking.domain.location.LocationRepository
import com.knotworking.domain.example.WordRepository
import com.knotworking.domain.location.GetLocationUseCase
import com.knotworking.domain.map.LoadMapUseCase
import com.knotworking.domain.map.MapRepository
import com.knotworking.tear.example.WordViewModel
import com.knotworking.tear.main.LocationViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ObsoleteCoroutinesApi
val presentationModule = module {
    viewModel { WordViewModel(getRandomWordUseCase = get()) }
    viewModel { LocationViewModel(getLocationUseCase = get(), loadMapUseCase = get()) }
}

val domainModule = module {
    //TODO move these to data modules
    single<WordRepository> { WordRepositoryImpl(wordDataSource = get()) }
    single<LocationRepository> { LocationRepositoryImpl(sharedLocationManager = get()) }
    single<MapRepository> { MapRepositoryImpl(mapDataSource = get()) }

    single { GetRandomWordUseCase(wordRepository = get()) }
    single { GetLocationUseCase(locationRepository = get()) }
    single { LoadMapUseCase(mapRepository = get()) }
}

val exampleDataModule = module {
    single<WordDataSource> { LocalWordDataSource() }
}

val locationDataModule = module {
    single { SharedLocationManager(context = androidContext(), externalScope = GlobalScope) }
}

val mapDataModule = module {
    //single<MapDataSource> { LocalMapDataSource(context = androidContext()) }
    single<MapDataSource> { InMemoryMapDataSource() }
}