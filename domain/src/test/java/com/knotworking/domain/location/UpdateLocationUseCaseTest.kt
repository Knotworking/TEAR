package com.knotworking.domain.location

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class UpdateLocationUseCaseTest {

    @MockK
    lateinit var mockLocationRepository: LocationRepository

    @InjectMockKs
    lateinit var sut: UpdateLocationUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `it should get a new location from the repository`() = runTest {
        // prepare
        val location = mockk<TrailLocation>()
        coEvery { mockLocationRepository.updateTrailLocation() } returns flowOf(location)

        // execute
        sut.invoke(Unit).test {
            assertEquals(location, awaitItem())
            awaitComplete()
        }

        // verify
        coVerify { mockLocationRepository.updateTrailLocation() }
    }
}