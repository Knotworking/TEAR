package com.knotworking.domain.usecase

import com.knotworking.domain.repository.WordRepository

class GetRandomWordUseCase(private val wordRepository: WordRepository) : BaseUseCase<None>() {
    override suspend fun run(params: None) {
        // Started loading
        resultChannel.send(Result.State.Loading)

        // Get word and send it, synchronous
        resultChannel.send(wordRepository.getRandomWord())

        resultChannel.send(Result.State.Loaded)
    }
}