package com.knotworking.domain.location

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {
    val receivingLocationUpdates: StateFlow<Boolean>

    @ExperimentalCoroutinesApi
    suspend fun updateTrailLocation(): Flow<TrailLocation>

    suspend fun getLastTrailLocation(): TrailLocation?

    suspend fun setMarkerText(text: String)

    suspend fun getMarkerText(): String
}