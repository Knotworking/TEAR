package com.knotworking.data

import com.knotworking.domain.repository.WordRepository
import com.knotworking.domain.usecase.Error
import com.knotworking.domain.usecase.Result

class WordRepositoryImpl(private val wordDataSource: WordDataSource): WordRepository {

    override suspend fun getRandomWord(): Result<String, Error> {
        return try {
            val word = wordDataSource.getRandomWord()
            Result.Success(word)
        } catch (error: Exception) {
            Result.Failure(Error.PersistenceError)
        }
    }
}