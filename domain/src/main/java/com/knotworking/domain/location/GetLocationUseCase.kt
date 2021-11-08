package com.knotworking.domain.location

import com.knotworking.domain.BaseUseCase
import kotlinx.coroutines.flow.Flow

typealias GetLocationBaseUseCase = BaseUseCase<Unit, Flow<Location>>

class GetLocationUseCase(private val locationRepository: LocationRepository) : GetLocationBaseUseCase {
    override suspend fun invoke(params: Unit): Flow<Location> = locationRepository.getLocation()
}