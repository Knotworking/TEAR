package com.knotworking.domain

import com.knotworking.domain.api.GetNewTokenUseCase
import com.knotworking.domain.api.PostLocationUseCase
import com.knotworking.domain.example.GetRandomWordUseCase
import com.knotworking.domain.location.GetLastLocationUseCase
import com.knotworking.domain.location.UpdateLocationUseCase
import com.knotworking.domain.location.marker.GetMarkerTextUseCase
import com.knotworking.domain.location.marker.SetMarkerTextUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GetRandomWordUseCase(wordRepository = get()) }
    single { UpdateLocationUseCase(locationRepository = get()) }
    single { GetNewTokenUseCase(apiRepository = get()) }
    single { PostLocationUseCase(apiRepository = get()) }
    single { GetLastLocationUseCase(locationRepository = get())}
    single { GetMarkerTextUseCase(locationRepository = get()) }
    single { SetMarkerTextUseCase(locationRepository = get()) }
}