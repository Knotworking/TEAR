package com.knotworking.domain.api

interface ApiRepository {
    suspend fun getNewToken(): Boolean

    suspend fun postCurrentLocation(): Boolean
}