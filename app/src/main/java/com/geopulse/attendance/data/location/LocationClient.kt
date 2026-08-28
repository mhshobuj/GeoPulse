package com.geopulse.attendance.data.location

import com.geopulse.attendance.domain.model.LocationCoordinates
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(intervalMs: Long): Flow<LocationCoordinates>
    suspend fun getCurrentLocation(): LocationCoordinates?
}
