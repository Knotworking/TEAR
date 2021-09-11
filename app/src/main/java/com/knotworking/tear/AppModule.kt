package com.knotworking.tear

import com.knotworking.data.location.LocationRepositoryImpl
import com.knotworking.data.location.SharedLocationManager
import com.knotworking.data.words.LocalWordDataSource
import com.knotworking.data.words.WordDataSource
import com.knotworking.data.words.WordRepositoryImpl
import com.knotworking.domain.location.LocationRepository
import com.knotworking.domain.repository.WordRepository
import com.knotworking.domain.usecase.GetLocationUseCase
import com.knotworking.domain.usecase.GetRandomWordUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ObsoleteCoroutinesApi
val viewModelModule = module {
    viewModel { WordViewModel(get()) }
    viewModel { LocationViewModel(get()) }
}

val useCaseModule = module {
    single { GetRandomWordUseCase(get()) }
    single { GetLocationUseCase(get()) }
}

val repositoryModule = module {
    single<WordRepository> { WordRepositoryImpl(wordDataSource = get()) }
    single<LocationRepository> { LocationRepositoryImpl(sharedLocationManager = get()) }
}

val localDataModule = module {
    single<WordDataSource> { LocalWordDataSource() }
}

val locationModule = module {
    single { SharedLocationManager(context = androidContext(), externalScope = GlobalScope) }
}