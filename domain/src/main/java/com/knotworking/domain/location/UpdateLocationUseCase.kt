package com.knotworking.domain.location

import com.knotworking.domain.BaseUseCase
import kotlinx.coroutines.flow.Flow

typealias UpdateLocationBaseUseCase = BaseUseCase<Unit, Flow<TrailLocation>>

class UpdateLocationUseCase(private val locationRepository: LocationRepository) : UpdateLocationBaseUseCase {
    override suspend fun invoke(params: Unit): Flow<TrailLocation> = locationRepository.updateTrailLocation()
}