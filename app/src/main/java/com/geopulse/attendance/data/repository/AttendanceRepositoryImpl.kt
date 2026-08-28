package com.geopulse.attendance.data.repository

import com.geopulse.attendance.data.local.dao.AttendanceDao
import com.geopulse.attendance.data.local.dao.OfficeLocationDao
import com.geopulse.attendance.data.local.entity.AttendanceEntity
import com.geopulse.attendance.data.local.entity.OfficeLocationEntity
import com.geopulse.attendance.domain.model.AttendanceRecord
import com.geopulse.attendance.domain.model.LocationCoordinates
import com.geopulse.attendance.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val officeLocationDao: OfficeLocationDao,
    private val attendanceDao: AttendanceDao
) : AttendanceRepository {

    override fun getSavedOfficeLocation(): Flow<LocationCoordinates?> {
        return officeLocationDao.getOfficeLocation().map { entity ->
            entity?.let {
                LocationCoordinates(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    timestamp = it.updatedTimestamp
                )
            }
        }
    }

    override suspend fun saveOfficeLocation(location: LocationCoordinates) {
        val entity = OfficeLocationEntity(
            id = 1,
            latitude = location.latitude,
            longitude = location.longitude,
            updatedTimestamp = System.currentTimeMillis()
        )
        officeLocationDao.insertOrUpdateOfficeLocation(entity)
    }

    override suspend fun recordAttendance(attendanceRecord: AttendanceRecord): Long {
        val entity = AttendanceEntity(
            timestamp = attendanceRecord.timestamp,
            latitude = attendanceRecord.latitude,
            longitude = attendanceRecord.longitude,
            distanceMeters = attendanceRecord.distanceMeters,
            status = attendanceRecord.status
        )
        return attendanceDao.insertAttendance(entity)
    }

    override fun getAttendanceHistory(): Flow<List<AttendanceRecord>> {
        return attendanceDao.getAllAttendanceRecords().map { entities ->
            entities.map { entity ->
                AttendanceRecord(
                    id = entity.id,
                    timestamp = entity.timestamp,
                    latitude = entity.latitude,
                    longitude = entity.longitude,
                    distanceMeters = entity.distanceMeters,
                    status = entity.status
                )
            }
        }
    }
}
