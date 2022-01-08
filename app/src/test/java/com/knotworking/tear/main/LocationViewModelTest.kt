package com.knotworking.tear.main

import app.cash.turbine.test
import com.knotworking.domain.api.PostLocationUseCase
import com.knotworking.domain.location.GetLastLocationUseCase
import com.knotworking.domain.location.TrailLocation
import com.knotworking.domain.location.UpdateLocationUseCase
import com.knotworking.tear.MainCoroutineRule
import com.knotworking.tear.runBlockingTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
internal class LocationViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @MockK
    lateinit var mockUpdateLocationUseCase: UpdateLocationUseCase

    @MockK
    lateinit var mockPostLocationUseCase: PostLocationUseCase

    @MockK
    lateinit var mockGetLastLocationUseCase: GetLastLocationUseCase

    @InjectMockKs
    lateinit var sut: LocationViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `load last location automatically`() = mainCoroutineRule.runBlockingTest {
        // prepare
        val location = TrailLocation(
            latitude = 42.46546753,
            longitude = 27.324345,
            kmProgress = 12.5,
            percentageProgress = 3.7,
            metresToTrail = 300.0,
            updatedAtSeconds = 1000L
        )
        coEvery { mockGetLastLocationUseCase(Unit) } returns location

        // execute
        sut.locationViewState.test {
            val actual = awaitItem()

            // verify
            assertFalse(actual.loadingLocation)
            assertFalse(actual.receivingUpdates)
            assertEquals(location.latitude, actual.latitude)
            assertEquals(location.longitude, actual.longitude)
            assertEquals(location.kmProgress, actual.kmProgress)
            assertEquals(location.percentageProgress, actual.percentageProgress)
            assertEquals(location.metresToTrail, actual.distanceToTrail)
            assertEquals(Instant.ofEpochSecond(location.updatedAtSeconds), actual.updatedAt)

            // call awaitItem() for next state
            // remove this comment when a test has been added with this
        }
    }

}