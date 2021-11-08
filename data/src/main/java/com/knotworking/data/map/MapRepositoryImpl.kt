package com.knotworking.data.map

import com.knotworking.domain.map.MapRepository

class MapRepositoryImpl(private val mapDataSource: MapDataSource) : MapRepository {
    override suspend fun getClosestKmMarker() = mapDataSource.getClosestKmMarker()
}