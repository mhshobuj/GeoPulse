package com.geopulse.attendance.di

import com.geopulse.attendance.data.repository.AttendanceRepositoryImpl
import com.geopulse.attendance.data.repository.LocationRepositoryImpl
import com.geopulse.attendance.domain.repository.AttendanceRepository
import com.geopulse.attendance.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(
        impl: AttendanceRepositoryImpl
    ): AttendanceRepository
}
