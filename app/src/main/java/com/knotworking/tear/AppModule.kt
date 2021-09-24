package com.knotworking.tear

import com.knotworking.data.location.LocationRepositoryImpl
import com.knotworking.data.location.SharedLocationManager
import com.knotworking.data.words.LocalWordDataSource
import com.knotworking.data.words.WordDataSource
import com.knotworking.data.words.WordRepositoryImpl
import com.knotworking.domain.example.GetRandomWordUseCase
import com.knotworking.domain.location.LocationRepository
import com.knotworking.domain.example.WordRepository
import com.knotworking.domain.location.GetLocationUseCase
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
    viewModel { LocationViewModel(getLocationUseCase = get()) }
}

val domainModule = module {
    single<WordRepository> { WordRepositoryImpl(wordDataSource = get()) }
    single<LocationRepository> { LocationRepositoryImpl(sharedLocationManager = get()) }

    single { GetRandomWordUseCase(wordRepository = get()) }
    single { GetLocationUseCase(locationRepository = get()) }
}

val exampleDataModule = module {
    single<WordDataSource> { LocalWordDataSource() }
}

val locationDataModule = module {
    single { SharedLocationManager(context = androidContext(), externalScope = GlobalScope) }
}