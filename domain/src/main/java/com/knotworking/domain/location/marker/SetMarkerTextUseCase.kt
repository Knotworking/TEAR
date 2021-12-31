package com.knotworking.domain.location.marker

import com.knotworking.domain.BaseUseCase
import com.knotworking.domain.location.LocationRepository

typealias SetMarkerTextBaseUseCase = BaseUseCase<String, Unit>

class SetMarkerTextUseCase(private val locationRepository: LocationRepository) :
    SetMarkerTextBaseUseCase {
    override suspend fun invoke(params: String) = locationRepository.setMarkerText(params)
}