package com.geopulse.attendance.ui.attendance

sealed class AttendanceUiEvent {
    object SetOfficeLocation : AttendanceUiEvent()
    object MarkAttendance : AttendanceUiEvent()
    object PermissionGranted : AttendanceUiEvent()
    object PermissionDenied : AttendanceUiEvent()
    object UserMessageDismissed : AttendanceUiEvent()
}
