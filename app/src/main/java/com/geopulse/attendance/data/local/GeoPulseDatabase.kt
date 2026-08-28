package com.geopulse.attendance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geopulse.attendance.data.local.dao.AttendanceDao
import com.geopulse.attendance.data.local.dao.OfficeLocationDao
import com.geopulse.attendance.data.local.entity.AttendanceEntity
import com.geopulse.attendance.data.local.entity.OfficeLocationEntity

@Database(
    entities = [
        OfficeLocationEntity::class,
        AttendanceEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GeoPulseDatabase : RoomDatabase() {
    abstract fun officeLocationDao(): OfficeLocationDao
    abstract fun attendanceDao(): AttendanceDao
}
