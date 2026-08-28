package com.geopulse.attendance.domain.usecase

import com.geopulse.attendance.domain.model.GeofenceStatus
import com.geopulse.attendance.domain.model.LocationCoordinates
import javax.inject.Inject

class CalculateDistanceUseCase @Inject constructor() {
    
    companion object {
        const val GEOFENCE_RADIUS_METERS = 50.0
    }

    operator fun invoke(
        currentLocation: LocationCoordinates?,
        officeLocation: LocationCoordinates?
    ): GeofenceStatus {
        if (currentLocation == null || officeLocation == null) {
            return GeofenceStatus.Unconfigured
        }

        val distance = currentLocation.distanceTo(officeLocation)
        return if (distance <= GEOFENCE_RADIUS_METERS) {
            GeofenceStatus.InRange(distance)
        } else {
            GeofenceStatus.OutOfRange(distance)
        }
    }
}
