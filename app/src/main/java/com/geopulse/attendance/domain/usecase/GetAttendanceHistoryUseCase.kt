package com.geopulse.attendance.domain.usecase

import com.geopulse.attendance.domain.model.AttendanceRecord
import com.geopulse.attendance.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAttendanceHistoryUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) {
    operator fun invoke(): Flow<List<AttendanceRecord>> {
        return attendanceRepository.getAttendanceHistory()
    }
}
