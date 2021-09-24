package com.knotworking.domain.example

import com.knotworking.domain.BaseUseCase
import kotlinx.coroutines.flow.Flow

typealias GetRandomWordBaseUseCase = BaseUseCase<Unit, Flow<String>>

class GetRandomWordUseCase(private val wordRepository: WordRepository) : GetRandomWordBaseUseCase {
    override suspend fun invoke(params: Unit): Flow<String> = wordRepository.getRandomWord()
}