package com.knotworking.domain.location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {
    val receivingLocationUpdates: StateFlow<Boolean>

    fun getLocations(): Flow<Location>
}