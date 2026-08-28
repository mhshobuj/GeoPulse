package com.geopulse.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.geopulse.attendance.data.local.entity.OfficeLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfficeLocationDao {
    @Query("SELECT * FROM office_location WHERE id = 1 LIMIT 1")
    fun getOfficeLocation(): Flow<OfficeLocationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateOfficeLocation(officeLocation: OfficeLocationEntity)
}
