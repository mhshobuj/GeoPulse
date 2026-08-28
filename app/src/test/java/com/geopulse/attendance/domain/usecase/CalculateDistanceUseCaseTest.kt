package com.geopulse.attendance.domain.usecase

import com.geopulse.attendance.domain.model.GeofenceStatus
import com.geopulse.attendance.domain.model.LocationCoordinates
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CalculateDistanceUseCaseTest {

    private lateinit var useCase: CalculateDistanceUseCase

    @Before
    fun setUp() {
        useCase = CalculateDistanceUseCase()
    }

    @Test
    fun `when currentLocation or officeLocation is null returns Unconfigured`() {
        val result = useCase(null, null)
        assertTrue(result is GeofenceStatus.Unconfigured)
    }

    @Test
    fun `when coordinates are identical returns InRange with 0 meters`() {
        val location = LocationCoordinates(23.7907, 90.3672)
        val result = useCase(location, location)
        assertTrue(result is GeofenceStatus.InRange)
        val inRange = result as GeofenceStatus.InRange
        assertEquals(0.0, inRange.distanceMeters, 0.01)
    }

    @Test
    fun `when distance is greater than 50 meters returns OutOfRange`() {
        val office = LocationCoordinates(23.7907, 90.3672)
        // Point roughly 110 meters away
        val current = LocationCoordinates(23.7917, 90.3672)
        val result = useCase(current, office)
        assertTrue(result is GeofenceStatus.OutOfRange)
    }
}
