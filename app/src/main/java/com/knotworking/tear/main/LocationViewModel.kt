package com.knotworking.tear.main

import android.util.Log
import com.knotworking.domain.location.GetLocationUseCase
import com.knotworking.tear.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

class LocationViewModel(
    private val getLocationUseCase: GetLocationUseCase
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
        locationFlow = launchInViewModelScope {
            _locationViewState.emit(
                _locationViewState.value.copy(
                    receivingUpdates = true,
                    loading = true
                )
            )
            getLocationUseCase(Unit).onStart {
                // Code only comes here after the first (and only) value is emitted
//                Log.i("TAG","Fetching latest location")
//                _locationViewState.value =
//                    LocationViewState(receivingUpdates = true, loading = true)
            }.catch {
                Log.e("TAG", "Error fetching trail location in viewmodel: ${it.message}")
                _locationViewState.value = LocationViewState(hasError = true)
            }.first { true }.also {
                stopLocationUpdates()
                _locationViewState.value =
                    LocationViewState(
                        receivingUpdates = false,
                        loading = false,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        kmProgress = it.kmProgress,
                        percentageProgress = it.percentageProgress,
                        distanceToTrail = it.metresToTrail
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
        val longitude: Double? = null,
        val kmProgress: Double? = null,
        val percentageProgress: Double? = null,
        val distanceToTrail: Double? = null
    )
}