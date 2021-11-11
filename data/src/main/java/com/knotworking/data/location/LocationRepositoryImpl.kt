package com.knotworking.data.location

import com.knotworking.data.route.RouteDataSource
import com.knotworking.domain.location.Location
import com.knotworking.domain.location.LocationRepository
import com.knotworking.domain.location.TrailLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class LocationRepositoryImpl(
    private val sharedLocationManager: SharedLocationManager,
    private val routeDataSource: RouteDataSource
) : LocationRepository {
    /**
     * Status of whether the app is actively subscribed to location changes.
     */
    override val receivingLocationUpdates: StateFlow<Boolean> =
        sharedLocationManager.receivingLocationUpdates

    /**
     * Get the users current location and calculate their trail position
     */
    @ExperimentalCoroutinesApi
    override suspend fun getTrailLocation(): Flow<TrailLocation> {
        val location = sharedLocationManager.locationFlow().first()
        delay(1000L)
        return flow { emit(routeDataSource.getTrailLocation(Location(location.latitude, location.longitude))) }
//        sharedLocationManager.locationFlow().map { Location(it.latitude, it.longitude) }
    }

}