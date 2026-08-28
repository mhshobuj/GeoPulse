package com.geopulse.attendance.domain.repository

import com.geopulse.attendance.domain.model.LocationCoordinates
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(intervalMs: Long = 3000L): Flow<LocationCoordinates>
    suspend fun getCurrentLocation(): LocationCoordinates?
}
