package com.knotworking.domain.map

import com.knotworking.domain.BaseUseCase

typealias LoadMapBaseUseCase = BaseUseCase<Unit, Int>

class LoadMapUseCase(private val mapRepository: MapRepository) : LoadMapBaseUseCase {
    override suspend fun invoke(params: Unit): Int = mapRepository.getClosestKmMarker()
}