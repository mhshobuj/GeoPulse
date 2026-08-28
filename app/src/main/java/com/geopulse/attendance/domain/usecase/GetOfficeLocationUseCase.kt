package com.geopulse.attendance.domain.usecase

import com.geopulse.attendance.domain.model.LocationCoordinates
import com.geopulse.attendance.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOfficeLocationUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) {
    operator fun invoke(): Flow<LocationCoordinates?> {
        return attendanceRepository.getSavedOfficeLocation()
    }
}
