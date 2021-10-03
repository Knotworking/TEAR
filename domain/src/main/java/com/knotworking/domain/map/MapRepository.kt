package com.knotworking.domain.map

interface MapRepository {
    suspend fun parseMapData()
}