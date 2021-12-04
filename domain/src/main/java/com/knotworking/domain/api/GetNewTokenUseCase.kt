package com.knotworking.domain.api

import com.knotworking.domain.BaseUseCase

typealias GetNewTokenBaseUseCase = BaseUseCase<Unit, Boolean>

class GetNewTokenUseCase(private val apiRepository: ApiRepository) : GetNewTokenBaseUseCase {
    override suspend fun invoke(params: Unit): Boolean = apiRepository.getNewToken()
}