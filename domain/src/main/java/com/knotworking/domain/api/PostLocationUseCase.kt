package com.knotworking.domain.api

import com.knotworking.domain.BaseUseCase


typealias PostLocationBaseUseCase = BaseUseCase<Unit, Boolean>

class PostLocationUseCase(private val apiRepository: ApiRepository) : PostLocationBaseUseCase {
    override suspend fun invoke(params: Unit): Boolean =
        apiRepository.postCurrentLocation()
}