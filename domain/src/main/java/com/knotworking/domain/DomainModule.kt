package com.knotworking.domain

import com.knotworking.domain.api.GetNewTokenUseCase
import com.knotworking.domain.api.PostLocationUseCase
import com.knotworking.domain.example.GetRandomWordUseCase
import com.knotworking.domain.location.GetLocationUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GetRandomWordUseCase(wordRepository = get()) }
    single { GetLocationUseCase(locationRepository = get()) }
    single { GetNewTokenUseCase(apiRepository = get()) }
    single { PostLocationUseCase(apiRepository = get()) }
}