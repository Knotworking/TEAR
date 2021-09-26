package com.knotworking.data.words

import app.cash.turbine.test
import com.knotworking.domain.example.WordRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class WordsRepositoryImplTest {

    @MockK
    lateinit var mockWordDataSource: WordDataSource

    lateinit var sut: WordRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = WordRepositoryImpl(wordDataSource = mockWordDataSource)
    }

    @Test
    fun `it should load a word from the datasource`() = runBlockingTest {
        // prepare
        coEvery { mockWordDataSource.getRandomWord() } returns "Test"

        // execute
        sut.getRandomWord().test {
            Assert.assertEquals(awaitItem(), "Test")
            awaitComplete()
        }

        // verify
        coVerify { mockWordDataSource.getRandomWord() }
    }
}