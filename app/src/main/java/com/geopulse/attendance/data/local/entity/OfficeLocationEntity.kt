package com.geopulse.attendance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "office_location")
data class OfficeLocationEntity(
    @PrimaryKey val id: Int = 1,
    val latitude: Double,
    val longitude: Double,
    val updatedTimestamp: Long
)
