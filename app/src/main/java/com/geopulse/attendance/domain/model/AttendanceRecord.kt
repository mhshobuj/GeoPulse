package com.geopulse.attendance.domain.model

data class AttendanceRecord(
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val latitude: Double,
    val longitude: Double,
    val distanceMeters: Double,
    val status: String = "SUCCESS"
)
