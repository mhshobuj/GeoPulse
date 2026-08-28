package com.geopulse.attendance.domain.usecase

import com.geopulse.attendance.domain.model.LocationCoordinates
import com.geopulse.attendance.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(intervalMs: Long = 3000L): Flow<LocationCoordinates> {
        return locationRepository.getLocationUpdates(intervalMs)
    }
}
