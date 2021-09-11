package com.knotworking.domain.usecase

import com.knotworking.domain.location.Location
import com.knotworking.domain.location.LocationRepository
import kotlinx.coroutines.flow.Flow

typealias GetLocationBaseUseCase = BaseUseCase<Unit, Flow<Location>>

class GetLocationUseCase(private val locationRepository: LocationRepository) : GetLocationBaseUseCase {
    override suspend fun invoke(params: Unit): Flow<Location> = locationRepository.getLocations()
}