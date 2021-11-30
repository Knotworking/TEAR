package com.knotworking.domain.location

import com.knotworking.domain.BaseUseCase
import kotlinx.coroutines.flow.Flow

typealias GetLocationBaseUseCase = BaseUseCase<Unit, Flow<TrailLocation>>

class GetLocationUseCase(private val locationRepository: LocationRepository) : GetLocationBaseUseCase {
    override suspend fun invoke(params: Unit): Flow<TrailLocation> = locationRepository.getTrailLocation()
}