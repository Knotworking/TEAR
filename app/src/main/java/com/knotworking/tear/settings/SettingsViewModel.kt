package com.knotworking.tear.settings

import com.knotworking.domain.api.GetNewTokenUseCase
import com.knotworking.domain.location.marker.GetMarkerTextUseCase
import com.knotworking.domain.location.marker.SetMarkerTextUseCase
import com.knotworking.tear.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val getNewTokenUseCase: GetNewTokenUseCase,
    private val getMarkerTextUseCase: GetMarkerTextUseCase,
    private val setMarkerTextUseCase: SetMarkerTextUseCase
): BaseViewModel() {
    val settingsViewState: StateFlow<SettingsViewState> by lazy {
        loadInitialState()
        return@lazy _settingsViewState
    }

    private var _settingsViewState = MutableStateFlow(
        SettingsViewState()
    )

    override val coroutineExceptionHandler = CoroutineExceptionHandler {_, throwable ->
        // do something
    }

    private fun loadInitialState() {
        launchInViewModelScope {
            val markerText = getMarkerTextUseCase.invoke(Unit)
            _settingsViewState.emit(
                _settingsViewState.value.copy(
                    markerText = markerText
                )
            )
        }
    }

    fun getNewToken() {
        launchInViewModelScope {
            _settingsViewState.emit(
                _settingsViewState.value.copy(
                    loginInProgress = true
                )
            )

            val success = getNewTokenUseCase.invoke(Unit)
            _settingsViewState.emit(
                _settingsViewState.value.copy(
                    loginInProgress = false
                )
            )
        }
    }

    fun setMarkerText(text: String) {
        launchInViewModelScope {
            //TODO progress button
            setMarkerTextUseCase.invoke(text)
            _settingsViewState.emit(
                _settingsViewState.value.copy(
                    markerText = text
                )
            )
        }
    }

    data class SettingsViewState(
        val loginInProgress: Boolean = false,
        val markerText: String = ""
    )
}