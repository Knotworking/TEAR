package com.knotworking.tear.main

import android.util.Log
import com.knotworking.domain.api.PostLocationUseCase
import com.knotworking.domain.location.GetLastLocationUseCase
import com.knotworking.domain.location.TrailLocation
import com.knotworking.domain.location.UpdateLocationUseCase
import com.knotworking.tear.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import java.time.Instant

class LocationViewModel(
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val postLocationUseCase: PostLocationUseCase,
    private val getLastLocationUseCase: GetLastLocationUseCase
) : BaseViewModel() {
    val locationViewState: StateFlow<LocationViewState> by lazy {
        loadLastLocation()
        return@lazy _locationViewState
    }

    private var _locationViewState = MutableStateFlow(
        LocationViewState()
    )

    // Get a reference to the Job from the Flow so we can stop it from UI events
    private var locationFlow: Job? = null

    override val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _locationViewState.value = _locationViewState.value.copy(
            hasError = true,
            loadingLocation = false,
            postingLocation = false,
            snackbarText = throwable.message
        )
    }

    private fun loadLastLocation() {
        launchInViewModelScope {
            val trailLocation = getLastLocationUseCase.invoke(Unit)
            trailLocation?.let {
                onNewLocation(it)
            }
        }
    }

    fun updateLocation() {
        locationFlow = launchInViewModelScope {
            _locationViewState.emit(
                _locationViewState.value.copy(
                    receivingUpdates = true,
                    loadingLocation = true
                )
            )
            updateLocationUseCase(Unit).onStart {
                // Code only comes here after the first (and only) value is emitted
//                Log.i("TAG","Fetching latest location")
//                _locationViewState.value =
//                    LocationViewState(receivingUpdates = true, loading = true)
            }.catch {
                Log.e("TAG", "Error fetching trail location in viewmodel: ${it.message}")
                _locationViewState.value = LocationViewState(hasError = true)
            }.first().also {
                stopLocationUpdates()
                onNewLocation(it)
            }
        }
    }

    private fun onNewLocation(trailLocation: TrailLocation) {
        _locationViewState.value =
            LocationViewState(
                receivingUpdates = false,
                loadingLocation = false,
                latitude = trailLocation.latitude,
                longitude = trailLocation.longitude,
                kmProgress = trailLocation.kmProgress,
                percentageProgress = trailLocation.percentageProgress,
                distanceToTrail = trailLocation.metresToTrail,
                updatedAt = Instant.ofEpochSecond(trailLocation.updatedAtSeconds)
            )
    }

    fun stopLocationUpdates() {
        locationFlow?.cancel()
        launchInViewModelScope {
            // keep the last value
            _locationViewState.emit(_locationViewState.value.copy(receivingUpdates = false))
        }
    }

    fun postLocation() {
        launchInViewModelScope {
            _locationViewState.emit(_locationViewState.value.copy(postingLocation = true))
            val success = postLocationUseCase.invoke(Unit)
            _locationViewState.emit(_locationViewState.value.copy(postingLocation = false))
            if (success) {
                showSnackbar("Location successfully updated.")
                Log.d("TAG", "location update successful")
            } else {
                showSnackbar("Unable to update location.")
            }
        }
    }

    fun showSnackbar(message: String) {
        launchInViewModelScope {
            _locationViewState.emit(
                _locationViewState.value.copy(
                    snackbarText = message
                )
            )
        }
    }

    fun hideSnackbar() {
        launchInViewModelScope {
            _locationViewState.emit(
                _locationViewState.value.copy(
                    snackbarText = null
                )
            )
        }
    }

    data class LocationViewState(
        val hasError: Boolean = false,
        val loadingLocation: Boolean = false,
        val postingLocation: Boolean = false,
        val snackbarText: String? = null,
        val receivingUpdates: Boolean = false,
        val latitude: Double? = null,
        val longitude: Double? = null,
        val kmProgress: Double? = null,
        val percentageProgress: Double? = null,
        val distanceToTrail: Double? = null,
        val updatedAt: Instant? = null
    )
}