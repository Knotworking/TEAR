package com.knotworking.domain.example

import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun getRandomWord(): Flow<String>
}