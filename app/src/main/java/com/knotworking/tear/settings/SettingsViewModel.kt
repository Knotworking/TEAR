package com.knotworking.tear.settings

import com.knotworking.domain.api.GetNewTokenUseCase
import com.knotworking.tear.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val getNewTokenUseCase: GetNewTokenUseCase
): BaseViewModel() {
    val settingsViewState: StateFlow<SettingsViewState>
    get() = _settingsViewState
    private var _settingsViewState = MutableStateFlow(
        SettingsViewState()
    )

    override val coroutineExceptionHandler = CoroutineExceptionHandler {_, throwable ->
        // do something
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

    data class SettingsViewState(
        val loginInProgress: Boolean = false
    )
}