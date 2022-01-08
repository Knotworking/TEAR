package com.knotworking.tear

import com.knotworking.tear.main.LocationViewModel
import com.knotworking.tear.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
val presentationModule = module {
    viewModel {
        LocationViewModel(
            updateLocationUseCase = get(),
            postLocationUseCase = get(),
            getLastLocationUseCase = get()
        )
    }
    viewModel {
        SettingsViewModel(
            getNewTokenUseCase = get(),
            getMarkerTextUseCase = get(),
            setMarkerTextUseCase = get()
        )
    }
}