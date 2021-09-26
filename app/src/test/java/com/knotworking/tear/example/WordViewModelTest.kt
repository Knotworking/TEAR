package com.knotworking.tear.example

import app.cash.turbine.test
import com.knotworking.domain.example.GetRandomWordUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
internal class WordViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var mockGetRandomWordUseCase: GetRandomWordUseCase

    @InjectMockKs
    lateinit var sut: WordViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { mockGetRandomWordUseCase(Unit) } returns flowOf("Test").map {
            delay(100)
            it
        }
    }

    @Test
    fun `requesting a new word should first show loading and then word`() = mainCoroutineRule.runBlockingTest {
        sut.requestNewWord()
        sut.wordViewState
            .test {
                var actual = awaitItem()
                assertTrue(actual.loading)
                assertFalse(actual.hasError)

                actual = awaitItem()
                assertEquals("Test", actual.word)
                assertFalse(actual.hasError)
            }
    }
}