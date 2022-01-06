package com.knotworking.data.route

import com.knotworking.data.db.TearDatabase
import com.knotworking.domain.location.Location
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocalRouteDataSourceTest {

    @MockK(relaxed = true)
    lateinit var mockDatabase: TearDatabase

    @MockK
    lateinit var mockDistanceCalculator: DistanceCalculator

    private lateinit var sut: LocalRouteDataSource

    private val delta = 0.001

    private val km0 = KmMarker(1, 0, 42.704819, 27.899409)
    private val km1 = KmMarker(2, 1, 42.710019387, 27.893293661)
    private val km2 = KmMarker(3, 2, 42.715500429, 27.884982483)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = LocalRouteDataSource(
            database = mockDatabase,
            distanceCalculator = mockDistanceCalculator,
            totalKm = 2
        )
    }

    @Test
    fun `getKmProgress before trail`() = runBlocking {
        // prepare
        val location = Location(42.695691, 27.913419)
        val closestMarker = km0
        every { mockDistanceCalculator.distanceToKmMarker(location, closestMarker) } returns 1000.0
        every { mockDistanceCalculator.distanceToKmMarker(location, km1) } returns 2000.0
        coEvery { mockDatabase.kmMarkerDao().getForKm(1) } returns listOf(km1)

        // execute
        val actual = sut.getKmProgress(location = location, closestMarker)

        // verify
        assertEquals(0.0, actual, delta)
    }

    @Test
    fun `getKmProgress after trail`() = runBlocking {
        // prepare
        val location = Location(42.715500429, 27.884982483)
        val closestMarker = km2
        every { mockDistanceCalculator.distanceToKmMarker(location, closestMarker) } returns 1000.0
        every { mockDistanceCalculator.distanceToKmMarker(location, km1) } returns 2000.0
        coEvery { mockDatabase.kmMarkerDao().getForKm(1) } returns listOf(km1)

        // execute
        val actual = sut.getKmProgress(location = location, closestMarker)

        // verify
        assertEquals(sut.totalKm.toDouble(), actual, delta)
    }

    @Test
    fun `getKmProgress after first marker`() = runBlocking {
        // prepare
        val location = Location(42.705402, 27.896269)
        val closestMarker = km0
        every { mockDistanceCalculator.distanceToKmMarker(location, closestMarker) } returns 300.0
        every { mockDistanceCalculator.distanceToKmMarker(location, km1) } returns 700.0
        coEvery { mockDatabase.kmMarkerDao().getForKm(1) } returns listOf(km1)

        // execute
        val actual = sut.getKmProgress(location = location, closestMarker)

        // verify
        assertEquals(0.3, actual, delta)
    }

    @Test
    fun `getKmProgress before second marker`() = runBlocking {
        // prepare
        val location = Location(42.708802, 27.894058)
        val closestMarker = km1
        every { mockDistanceCalculator.distanceToKmMarker(location, closestMarker) } returns 300.0
        every { mockDistanceCalculator.distanceToKmMarker(location, km0) } returns 700.0
        every { mockDistanceCalculator.distanceToKmMarker(location, km2) } returns 1300.0
        coEvery { mockDatabase.kmMarkerDao().getForKm(0) } returns listOf(km0)
        coEvery { mockDatabase.kmMarkerDao().getForKm(2) } returns listOf(km2)

        // execute
        val actual = sut.getKmProgress(location = location, closestMarker)

        // verify
        assertEquals(0.7, actual, delta)
    }

    @Test
    fun `getKmProgress after second marker`() = runBlocking {
        // prepare
        val location = Location(42.711944, 27.890854)
        val closestMarker = km1
        every { mockDistanceCalculator.distanceToKmMarker(location, closestMarker) } returns 300.0
        every { mockDistanceCalculator.distanceToKmMarker(location, km0) } returns 1300.0
        every { mockDistanceCalculator.distanceToKmMarker(location, km2) } returns 700.0
        coEvery { mockDatabase.kmMarkerDao().getForKm(0) } returns listOf(km0)
        coEvery { mockDatabase.kmMarkerDao().getForKm(2) } returns listOf(km2)

        // execute
        val actual = sut.getKmProgress(location = location, closestMarker)

        // verify
        assertEquals(1.3, actual, delta)
    }

    @Test
    fun `getKmProgress for distant marker`() = runBlocking {
        // prepare
        val location = Location(42.711944, 27.890854)
        val closestMarker = km1
        every { mockDistanceCalculator.distanceToKmMarker(location, closestMarker) } returns 2001.0

        // execute
        val actual = sut.getKmProgress(location = location, closestMarker)

        // verify
        assertEquals(1.0, actual, delta)
    }

    @Test
    fun `check total kms`() {
        assertEquals(2, sut.totalKm)
    }
}