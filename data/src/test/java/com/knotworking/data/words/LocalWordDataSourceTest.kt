package com.knotworking.data.words

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LocalWordDataSourceTest {

    lateinit var sut: WordDataSource

    @Before
    fun setUp() {
        sut = LocalWordDataSource()
    }

    @Test
    fun `it should generate a random word`() = runBlocking {
        // execute
        val actual = sut.getRandomWord()

        // verify
        assertTrue(actual.startsWith("Test "))
    }
}