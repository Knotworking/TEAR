package com.knotworking.tear

import com.knotworking.data.LocalWordDataSource
import com.knotworking.data.WordDataSource
import com.knotworking.data.WordRepositoryImpl
import com.knotworking.domain.repository.WordRepository
import com.knotworking.domain.usecase.GetRandomWordUseCase
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ObsoleteCoroutinesApi
val viewModelModule = module {
    viewModel {WordViewModel(get())}
}

val useCaseModule = module {
    single { GetRandomWordUseCase(get()) }
}

val repositoryModule = module {
    single<WordRepository> { WordRepositoryImpl(wordDataSource = get()) }
}

val localDataModule = module {
    single<WordDataSource> { LocalWordDataSource() }
}