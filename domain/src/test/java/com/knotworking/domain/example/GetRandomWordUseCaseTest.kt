package com.knotworking.domain.example

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class GetRandomWordUseCaseTest {

    @MockK
    lateinit var mockWordRepository: WordRepository

    lateinit var sut: GetRandomWordUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = GetRandomWordUseCase(wordRepository = mockWordRepository)
    }

    @Test
    fun `it should load a word from the repository`() = runBlockingTest {
        // prepare
        coEvery { mockWordRepository.getRandomWord() } returns flowOf("Test")

        // execute
        sut.invoke(Unit).test {
            assertEquals(awaitItem(), "Test")
            awaitComplete()
        }

        // verify
        coVerify { mockWordRepository.getRandomWord() }
    }
}