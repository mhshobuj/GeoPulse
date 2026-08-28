package com.geopulse.attendance.domain.usecase

import com.geopulse.attendance.domain.model.LocationCoordinates
import com.geopulse.attendance.domain.repository.AttendanceRepository
import com.geopulse.attendance.domain.repository.LocationRepository
import javax.inject.Inject

class SetOfficeLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val attendanceRepository: AttendanceRepository
) {
    suspend operator fun invoke(): Result<LocationCoordinates> {
        val currentLocation = locationRepository.getCurrentLocation()
            ?: return Result.failure(Exception("Unable to fetch current GPS location. Please ensure location services are enabled."))

        attendanceRepository.saveOfficeLocation(currentLocation)
        return Result.success(currentLocation)
    }
}
