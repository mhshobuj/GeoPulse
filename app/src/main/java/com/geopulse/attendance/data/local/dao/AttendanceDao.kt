package com.geopulse.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geopulse.attendance.data.local.entity.AttendanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity): Long

    @Query("SELECT * FROM attendance_records ORDER BY timestamp DESC")
    fun getAllAttendanceRecords(): Flow<List<AttendanceEntity>>
}
