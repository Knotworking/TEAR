package com.knotworking.data.route

import android.util.Log
import com.knotworking.data.db.TearDatabase
import com.knotworking.domain.location.Location
import com.knotworking.domain.location.TrailLocation
import kotlinx.coroutines.*
import java.time.Instant

interface RouteDataSource {
    suspend fun getClosestKmMarker(location: Location): KmMarker?
    suspend fun getTrailLocation(location: Location): TrailLocation
    val totalKm: Int
}

// TODO can I just inject the relevant DAO?
class LocalRouteDataSource(
    private val database: TearDatabase,
    private val distanceCalculator: DistanceCalculator,
    override val totalKm: Int
) : RouteDataSource {

    override suspend fun getClosestKmMarker(location: Location): KmMarker? {
        val startTime = System.currentTimeMillis()
        val closestPoint = getClosestKmMarkerBruteForce(location)
        val timeTaken = System.currentTimeMillis() - startTime
        Log.i("TAG", "closest point: $closestPoint, loaded in ${timeTaken}ms")
        return closestPoint
    }

    override suspend fun getTrailLocation(location: Location): TrailLocation {
        val kmMarker = getClosestKmMarker(location)
        val kmProgress = getKmProgress(location = location, closestMarker = kmMarker)
        val percentage = (kmProgress / totalKm) * 100
        return TrailLocation(
            latitude = location.latitude,
            longitude = location.longitude,
            kmProgress = kmProgress,
            percentageProgress = percentage,
            metresToTrail = if (kmMarker != null) distanceCalculator.distanceToKmMarker(
                location,
                kmMarker
            ) else null,
            updatedAtSeconds = Instant.now().epochSecond
        )
    }

    // This only takes 100-300ms, so no need to implement a more sophisticated search algorithm
    @ExperimentalCoroutinesApi
    private suspend fun getClosestKmMarkerBruteForce(location: Location): KmMarker? {
        val allMarkers = database.kmMarkerDao().getAll()
        return allMarkers.minByOrNull { distanceCalculator.distanceToKmMarker(location, it) }
    }

    private suspend fun getKmMarkerByKm(km: Int): KmMarker {
        return database.kmMarkerDao().getForKm(km).first()
    }

    suspend fun getKmProgress(location: Location, closestMarker: KmMarker?): Double {
            if (closestMarker == null) {
                return 0.0
            }

            val distanceToMarker = distanceCalculator.distanceToKmMarker(location, closestMarker)

            if (distanceToMarker > 2000) {
                // just use closest km, as the calculation will be too inaccurate otherwise
                return closestMarker.km.toDouble()
            }

            val previousMarker = if (closestMarker.km > 0) getKmMarkerByKm(closestMarker.km - 1) else null
            val nextMarker = if (closestMarker.km < totalKm) getKmMarkerByKm(closestMarker.km + 1) else null

            if (previousMarker == null) {
                val distanceToNext = distanceCalculator.distanceToKmMarker(location, nextMarker!!)
                if (distanceToNext > 1000) {
                    //User is behind the start marker
                    return 0.0
                }
            }

            if (nextMarker == null) {
                val distanceToPrevious =
                    distanceCalculator.distanceToKmMarker(location, previousMarker!!)
                if (distanceToPrevious > 1000) {
                    //User is after the end
                    return totalKm.toDouble()
                }
            }

            val isAfterMarker = nextMarker != null &&
                    (previousMarker == null ||
                            distanceCalculator.distanceToKmMarker(
                                location,
                                nextMarker
                            ) < distanceCalculator.distanceToKmMarker(location, previousMarker))

            return if (isAfterMarker) {
                closestMarker.km + getProgressBetweenPoints(
                    location = location,
                    pointA = closestMarker,
                    pointB = nextMarker!!
                )
            } else {
                closestMarker.km - getProgressBetweenPoints(
                    location = location,
                    pointA = closestMarker,
                    pointB = previousMarker!!
                )
            }
        }

    private fun getProgressBetweenPoints(
        location: Location,
        pointA: KmMarker,
        pointB: KmMarker
    ): Double {
        val distToA = distanceCalculator.distanceToKmMarker(location, pointA)
        val distToB = distanceCalculator.distanceToKmMarker(location, pointB)

        val totalDistance = distToA + distToB

        return distToA / totalDistance
    }
}