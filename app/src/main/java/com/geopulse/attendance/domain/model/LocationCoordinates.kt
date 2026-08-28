package com.geopulse.attendance.domain.model

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class LocationCoordinates(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun distanceTo(other: LocationCoordinates): Double {
        val earthRadiusMeters = 6371000.0
        val dLat = Math.toRadians(other.latitude - latitude)
        val dLon = Math.toRadians(other.longitude - longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(latitude)) * cos(Math.toRadians(other.latitude)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusMeters * c
    }

    fun formattedLatLon(): String {
        return String.format("Lat: %.4f, Lon: %.4f", latitude, longitude)
    }
}
