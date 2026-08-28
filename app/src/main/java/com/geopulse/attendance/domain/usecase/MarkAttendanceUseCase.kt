package com.geopulse.attendance.domain.usecase

import com.geopulse.attendance.domain.model.AttendanceRecord
import com.geopulse.attendance.domain.model.LocationCoordinates
import com.geopulse.attendance.domain.repository.AttendanceRepository
import com.geopulse.attendance.domain.repository.LocationRepository
import javax.inject.Inject

class MarkAttendanceUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val attendanceRepository: AttendanceRepository,
    private val calculateDistanceUseCase: CalculateDistanceUseCase
) {
    suspend operator fun invoke(officeLocation: LocationCoordinates): Result<AttendanceRecord> {
        val currentLocation = locationRepository.getCurrentLocation()
            ?: return Result.failure(Exception("Could not retrieve your precise location."))

        val distance = currentLocation.distanceTo(officeLocation)

        if (distance > CalculateDistanceUseCase.GEOFENCE_RADIUS_METERS) {
            return Result.failure(
                Exception("You are ${distance.toInt()}m away. Must be within 50m of office to mark attendance.")
            )
        }

        val record = AttendanceRecord(
            timestamp = System.currentTimeMillis(),
            latitude = currentLocation.latitude,
            longitude = currentLocation.longitude,
            distanceMeters = distance,
            status = "MARKED"
        )

        attendanceRepository.recordAttendance(record)
        return Result.success(record)
    }
}
