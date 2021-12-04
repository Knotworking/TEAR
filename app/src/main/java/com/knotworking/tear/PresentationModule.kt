package com.knotworking.tear

import com.knotworking.tear.example.WordViewModel
import com.knotworking.tear.main.LocationViewModel
import com.knotworking.tear.settings.SettingsViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ObsoleteCoroutinesApi
val presentationModule = module {
    viewModel { WordViewModel(getRandomWordUseCase = get()) }
    viewModel { LocationViewModel(getLocationUseCase = get()) }
    viewModel { SettingsViewModel(getNewTokenUseCase = get()) }
}