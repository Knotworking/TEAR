package com.knotworking.data.route

import com.knotworking.domain.location.Location

interface DistanceCalculator {
    fun distanceToKmMarker(
        location: Location,
        it: KmMarker
    ): Double
}

class AndroidDistanceCalculator : DistanceCalculator {
    override fun distanceToKmMarker(location: Location, it: KmMarker): Double {
        val distance = FloatArray(1)
        android.location.Location.distanceBetween(
            location.latitude,
            location.longitude,
            it.latitude,
            it.longitude,
            distance
        )
        return distance[0].toDouble()
    }
}