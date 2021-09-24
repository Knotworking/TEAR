package com.knotworking.data.location

import com.knotworking.domain.location.Location
import com.knotworking.domain.location.LocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class LocationRepositoryImpl(
    private val sharedLocationManager: SharedLocationManager
) : LocationRepository {
    /**
     * Status of whether the app is actively subscribed to location changes.
     */
    override val receivingLocationUpdates: StateFlow<Boolean> =
        sharedLocationManager.receivingLocationUpdates

    /**
     * Observable flow for location updates
     */
    @ExperimentalCoroutinesApi
    override fun getLocations() =
        sharedLocationManager.locationFlow().map { Location(it.latitude, it.longitude) }
}