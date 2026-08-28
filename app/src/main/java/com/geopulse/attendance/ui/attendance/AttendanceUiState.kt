package com.geopulse.attendance.ui.attendance

import com.geopulse.attendance.domain.model.AttendanceRecord
import com.geopulse.attendance.domain.model.GeofenceStatus
import com.geopulse.attendance.domain.model.LocationCoordinates

data class AttendanceUiState(
    val currentLocation: LocationCoordinates? = null,
    val officeLocation: LocationCoordinates? = null,
    val geofenceStatus: GeofenceStatus = GeofenceStatus.Unconfigured,
    val geofenceRadiusMeters: Double = 50.0,
    val isSettingLocation: Boolean = false,
    val isMarkingAttendance: Boolean = false,
    val userMessage: String? = null,
    val attendanceHistory: List<AttendanceRecord> = emptyList(),
    val hasPermission: Boolean = false
)
