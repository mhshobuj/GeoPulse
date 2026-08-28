package com.geopulse.attendance.domain.repository

import com.geopulse.attendance.domain.model.AttendanceRecord
import com.geopulse.attendance.domain.model.LocationCoordinates
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    fun getSavedOfficeLocation(): Flow<LocationCoordinates?>
    suspend fun saveOfficeLocation(location: LocationCoordinates)
    suspend fun recordAttendance(attendanceRecord: AttendanceRecord): Long
    fun getAttendanceHistory(): Flow<List<AttendanceRecord>>
}
