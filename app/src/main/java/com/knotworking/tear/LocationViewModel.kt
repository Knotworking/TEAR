package com.knotworking.tear

import com.knotworking.domain.usecase.GetLocationUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*

internal class LocationViewModel(
    private val getLocationUseCase: GetLocationUseCase
) : BaseViewModel() {
    val locationViewState: StateFlow<LocationViewState>
        get() = _locationViewState
    private var _locationViewState = MutableStateFlow(
        LocationViewState()
    )

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _locationViewState.value = _locationViewState.value.copy(hasError = true)
    }

    fun startLocationUpdates() {
        launchInViewModelScope {
            getLocationUseCase(Unit).onStart {
                _locationViewState.value = LocationViewState(loading = true)
            }.catch {
                _locationViewState.value = LocationViewState(hasError = true)
            }.collect {
                _locationViewState.value =
                    LocationViewState(latitude = it.latitude, longitude = it.longitude)
            }
        }
    }

    fun stopLocationUpdates() {

    }

    data class LocationViewState(
        val hasError: Boolean = false,
        val loading: Boolean = false,
        val latitude: Double? = null,
        val longitude: Double? = null
    )
}