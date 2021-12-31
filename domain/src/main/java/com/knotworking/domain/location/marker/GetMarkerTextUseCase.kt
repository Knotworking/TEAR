package com.knotworking.domain.location.marker

import com.knotworking.domain.BaseUseCase
import com.knotworking.domain.location.LocationRepository

typealias GetMarkerTextBaseUseCase = BaseUseCase<Unit, String>

class GetMarkerTextUseCase(private val locationRepository: LocationRepository) :
    GetMarkerTextBaseUseCase {
    override suspend fun invoke(params: Unit): String = locationRepository.getMarkerText()
}