package com.knotworking.domain.repository

import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun getRandomWord(): Flow<String>
}