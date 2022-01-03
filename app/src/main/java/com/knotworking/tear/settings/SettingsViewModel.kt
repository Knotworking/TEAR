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
        _settingsViewState.value = _settingsViewState.value.copy(
            loginInProgress = false,
            snackbarText = throwable.message
        )
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
            if (success) {
                _settingsViewState.value = _settingsViewState.value.copy(
                    loginInProgress = false,
                    snackbarText = "Token retrieved."
                )
            } else {
                _settingsViewState.value = _settingsViewState.value.copy(
                    loginInProgress = false,
                    snackbarText = "Unable to get new token."
                )
            }
        }
    }

    fun setMarkerText(text: String) {
        launchInViewModelScope {
            setMarkerTextUseCase.invoke(text)
            _settingsViewState.emit(
                _settingsViewState.value.copy(
                    markerText = text
                )
            )
            showSnackbar("Marker text set. Remember to repost your location.")
        }
    }

    fun showSnackbar(message: String) {
        launchInViewModelScope {
            _settingsViewState.emit(
                _settingsViewState.value.copy(
                    snackbarText = message
                )
            )
        }
    }

    fun hideSnackbar() {
        launchInViewModelScope {
            _settingsViewState.emit(
                _settingsViewState.value.copy(
                    snackbarText = null
                )
            )
        }
    }

    data class SettingsViewState(
        val loginInProgress: Boolean = false,
        val markerText: String = "",
        val snackbarText: String? = null,
    )
}