package com.knotworking.data.words

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

interface WordDataSource {
    suspend fun getRandomWord(): String
}

class LocalWordDataSource : WordDataSource {
    override suspend fun getRandomWord(): String {
        val random = Random.Default

        return withContext(Dispatchers.IO) {
            val randomDelay = random.nextLong(2000)
            Thread.sleep(randomDelay)
            val newInt = random.nextInt(10)
            return@withContext "Test $newInt"
        }
    }

}