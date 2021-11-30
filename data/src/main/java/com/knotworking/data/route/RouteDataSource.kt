package com.knotworking.data.route

import android.util.Log
import com.knotworking.data.db.TearDatabase
import com.knotworking.domain.location.Location
import com.knotworking.domain.location.TrailLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.location.Location as AndroidLocation

interface RouteDataSource {
    suspend fun getClosestKmMarker(location: Location): KmMarker?
    suspend fun getTrailLocation(location: Location): TrailLocation
    val totalKm: Int
}

// TODO can I just inject the relevant DAO?
class LocalRouteDataSource(
    private val database: TearDatabase
) : RouteDataSource {

    override val totalKm = 6254

    override suspend fun getClosestKmMarker(location: Location): KmMarker? {
        val startTime = System.currentTimeMillis()
        val closestPoint = getClosestKmMarkerBruteForce(location)
        val timeTaken = System.currentTimeMillis() - startTime
        Log.i("TAG", "closest point: $closestPoint, loaded in ${timeTaken}ms")
        return closestPoint
    }

    override suspend fun getTrailLocation(location: Location): TrailLocation {
        val kmMarker = getClosestKmMarker(location)
        val kmProgress = kmMarker?.km?.toDouble()
        val percentage = if (kmProgress != null) {
            (kmProgress / totalKm) * 100
        } else {
            0.0
        }
        return TrailLocation(
            latitude = location.latitude,
            longitude = location.longitude,
            kmProgress = kmProgress ?: 0.0,
            percentageProgress = percentage,
            metresToTrail = if (kmMarker != null) distanceToKmMarker(location, kmMarker) else null
        )
    }

    // This only takes 100-300ms, so no need to implement a more sophisticated search algorithm
    @ExperimentalCoroutinesApi
    private suspend fun getClosestKmMarkerBruteForce(location: Location): KmMarker? {
        val allMarkers = database.kmMarkerDao().getAll()
        return allMarkers.minByOrNull { distanceToKmMarker(location, it) }
    }

    private fun distanceToKmMarker(
        location: Location,
        it: KmMarker
    ): Double {
        val distance = FloatArray(1)
        AndroidLocation.distanceBetween(
            location.latitude,
            location.longitude,
            it.latitude,
            it.longitude,
            distance
        )
        return distance[0].toDouble()
    }
}