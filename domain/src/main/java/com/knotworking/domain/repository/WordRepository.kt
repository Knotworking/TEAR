package com.knotworking.domain.repository

import com.knotworking.domain.usecase.Error
import com.knotworking.domain.usecase.Result

interface WordRepository {
    suspend fun getRandomWord(): Result<String, Error>
}