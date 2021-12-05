package com.knotworking.domain.api

import com.knotworking.domain.BaseUseCase
import com.knotworking.domain.location.Location


typealias PostLocationBaseUseCase = BaseUseCase<Location, Boolean>

class PostLocationUseCase(private val apiRepository: ApiRepository) : PostLocationBaseUseCase {
    override suspend fun invoke(params: Location): Boolean =
        apiRepository.setCurrentLocation(location = params)
}