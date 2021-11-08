package com.knotworking.data.map

import android.location.Location
import android.util.Log
import com.knotworking.data.db.TearDatabase
import com.knotworking.data.location.SharedLocationManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first

interface MapDataSource {
    suspend fun getClosestKmMarker(): Int
}

// TODO can I just inject the relevant DAO?
class LocalMapDataSource(
    private val database: TearDatabase,
    private val sharedLocationManager: SharedLocationManager
) : MapDataSource {

    override suspend fun getClosestKmMarker(): Int {
        val startTime = System.currentTimeMillis()
        val closestPoint = getClosestKmMarkerBruteForce()
        val timeTaken = System.currentTimeMillis() - startTime
        Log.i("TAG", "closest point: $closestPoint, loaded in ${timeTaken}ms")
        return closestPoint
    }

    @ExperimentalCoroutinesApi
    private suspend fun getClosestKmMarkerBruteForce(): Int {
        val location = sharedLocationManager.locationFlow().first()
        val allMarkers = database.kmMarkerDao().getAll()
        // Log.i("TAG", "loadMapData: ${allMarkers.size}")

        val marker = allMarkers.minByOrNull { distanceToKmMarker(location, it) }
        marker?.let {
        Log.i("TAG", "distance to marker: ${distanceToKmMarker(location, it)}m")
        }
        return marker?.km ?: -1
    }

    private fun distanceToKmMarker(
        location: Location,
        it: KmMarker
    ): Float {
        val distance = FloatArray(1)
        Location.distanceBetween(
            location.latitude,
            location.longitude,
            it.latitude,
            it.longitude,
            distance
        )
        return distance[0]
    }
}