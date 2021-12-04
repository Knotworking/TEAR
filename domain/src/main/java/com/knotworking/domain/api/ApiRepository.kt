package com.knotworking.domain.api

import com.knotworking.domain.location.Location

interface ApiRepository {
    suspend fun getNewToken(): Boolean

    suspend fun setCurrentLocation(location: Location)
}