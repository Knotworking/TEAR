package com.knotworking.domain.usecase

import com.knotworking.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow

typealias GetRandomWordBaseUseCase = BaseUseCase<Unit, Flow<String>>

class GetRandomWordUseCase(private val wordRepository: WordRepository) : GetRandomWordBaseUseCase {
    override suspend fun invoke(params: Unit): Flow<String> = wordRepository.getRandomWord()
}