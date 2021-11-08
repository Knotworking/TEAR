package com.knotworking.domain.map

interface MapRepository {
    suspend fun getClosestKmMarker() : Int
}