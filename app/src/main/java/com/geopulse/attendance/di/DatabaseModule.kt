package com.geopulse.attendance.di

import android.content.Context
import androidx.room.Room
import com.geopulse.attendance.data.local.GeoPulseDatabase
import com.geopulse.attendance.data.local.dao.AttendanceDao
import com.geopulse.attendance.data.local.dao.OfficeLocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGeoPulseDatabase(@ApplicationContext context: Context): GeoPulseDatabase {
        return Room.databaseBuilder(
            context,
            GeoPulseDatabase::class.java,
            "geopulse_attendance.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideOfficeLocationDao(db: GeoPulseDatabase): OfficeLocationDao {
        return db.officeLocationDao()
    }

    @Provides
    fun provideAttendanceDao(db: GeoPulseDatabase): AttendanceDao {
        return db.attendanceDao()
    }
}
