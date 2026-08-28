package com.geopulse.attendance.domain.model

sealed class GeofenceStatus {
    object Unconfigured : GeofenceStatus()
    data class InRange(val distanceMeters: Double) : GeofenceStatus()
    data class OutOfRange(val distanceMeters: Double) : GeofenceStatus()
}
