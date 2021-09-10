package com.knotworking.data

import com.knotworking.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WordRepositoryImpl(private val wordDataSource: WordDataSource): WordRepository {
    override fun getRandomWord(): Flow<String> = flow {
        val word = wordDataSource.getRandomWord()
        // here were would convert entities to domain models
        emit(word)
    }
}