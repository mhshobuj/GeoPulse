package com.geopulse.attendance.data.repository

import com.geopulse.attendance.data.location.LocationClient
import com.geopulse.attendance.domain.model.LocationCoordinates
import com.geopulse.attendance.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationClient: LocationClient
) : LocationRepository {

    override fun getLocationUpdates(intervalMs: Long): Flow<LocationCoordinates> {
        return locationClient.getLocationUpdates(intervalMs)
    }

    override suspend fun getCurrentLocation(): LocationCoordinates? {
        return locationClient.getCurrentLocation()
    }
}
