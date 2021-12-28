package com.knotworking.domain.location

import com.knotworking.domain.BaseUseCase

typealias GetLastLocationBaseUseCase = BaseUseCase<Unit, TrailLocation?>

class GetLastLocationUseCase(private val locationRepository: LocationRepository) : GetLastLocationBaseUseCase {
    override suspend fun invoke(params: Unit): TrailLocation? {
        return locationRepository.getLastTrailLocation()
    }
}