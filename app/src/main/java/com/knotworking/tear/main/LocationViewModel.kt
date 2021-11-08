package com.knotworking.tear.main

import com.knotworking.domain.location.GetLocationUseCase
import com.knotworking.domain.map.LoadMapUseCase
import com.knotworking.tear.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

internal class LocationViewModel(
    private val getLocationUseCase: GetLocationUseCase,
    private val loadMapUseCase: LoadMapUseCase
) : BaseViewModel() {
    val locationViewState: StateFlow<LocationViewState>
        get() = _locationViewState
    private var _locationViewState = MutableStateFlow(
        LocationViewState()
    )

    // Get a reference to the Job from the Flow so we can stop it from UI events
    private var locationFlow: Job? = null

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _locationViewState.value = _locationViewState.value.copy(hasError = true)
    }

    fun startLocationUpdates() {
        launchInViewModelScope {
            loadMapUseCase.invoke(Unit)
        }

        locationFlow = launchInViewModelScope {
            getLocationUseCase(Unit).onStart {
                _locationViewState.value =
                    LocationViewState(receivingUpdates = true, loading = true)
            }.catch {
                _locationViewState.value = LocationViewState(hasError = true)
            }.collect {
                _locationViewState.value =
                    LocationViewState(
                        receivingUpdates = true,
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
            }
        }
    }

    fun stopLocationUpdates() {
        locationFlow?.cancel()
        launchInViewModelScope {
            // keep the last value
            _locationViewState.emit(_locationViewState.value.copy(receivingUpdates = false))
        }
    }

    data class LocationViewState(
        val hasError: Boolean = false,
        val loading: Boolean = false,
        val receivingUpdates: Boolean = false,
        val latitude: Double? = null,
        val longitude: Double? = null
    )
}