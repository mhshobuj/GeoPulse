package com.geopulse.attendance.ui.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geopulse.attendance.domain.usecase.CalculateDistanceUseCase
import com.geopulse.attendance.domain.usecase.GetAttendanceHistoryUseCase
import com.geopulse.attendance.domain.usecase.GetOfficeLocationUseCase
import com.geopulse.attendance.domain.usecase.MarkAttendanceUseCase
import com.geopulse.attendance.domain.usecase.ObserveCurrentLocationUseCase
import com.geopulse.attendance.domain.usecase.SetOfficeLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val setOfficeLocationUseCase: SetOfficeLocationUseCase,
    private val getOfficeLocationUseCase: GetOfficeLocationUseCase,
    private val observeCurrentLocationUseCase: ObserveCurrentLocationUseCase,
    private val calculateDistanceUseCase: CalculateDistanceUseCase,
    private val markAttendanceUseCase: MarkAttendanceUseCase,
    private val getAttendanceHistoryUseCase: GetAttendanceHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    private var locationUpdatesJob: Job? = null

    init {
        observeOfficeLocation()
        observeAttendanceHistory()
    }

    fun onEvent(event: AttendanceUiEvent) {
        when (event) {
            is AttendanceUiEvent.SetOfficeLocation -> handleSetOfficeLocation()
            is AttendanceUiEvent.MarkAttendance -> handleMarkAttendance()
            is AttendanceUiEvent.PermissionGranted -> handlePermissionGranted()
            is AttendanceUiEvent.PermissionDenied -> handlePermissionDenied()
            is AttendanceUiEvent.UserMessageDismissed -> clearUserMessage()
        }
    }

    private fun handlePermissionGranted() {
        _uiState.update { it.copy(hasPermission = true) }
        startObservingLocationUpdates()
    }

    private fun handlePermissionDenied() {
        _uiState.update {
            it.copy(
                hasPermission = false,
                userMessage = "Location permission is required for geofenced attendance."
            )
        }
    }

    private fun startObservingLocationUpdates() {
        locationUpdatesJob?.cancel()
        locationUpdatesJob = viewModelScope.launch {
            observeCurrentLocationUseCase()
                .catch { e ->
                    _uiState.update { it.copy(userMessage = e.message ?: "Failed to get location updates") }
                }
                .collect { location ->
                    _uiState.update { currentState ->
                        val status = calculateDistanceUseCase(location, currentState.officeLocation)
                        currentState.copy(
                            currentLocation = location,
                            geofenceStatus = status
                        )
                    }
                }
        }
    }

    private fun observeOfficeLocation() {
        viewModelScope.launch {
            getOfficeLocationUseCase().collect { officeLoc ->
                _uiState.update { currentState ->
                    val status = calculateDistanceUseCase(currentState.currentLocation, officeLoc)
                    currentState.copy(
                        officeLocation = officeLoc,
                        geofenceStatus = status
                    )
                }
            }
        }
    }

    private fun observeAttendanceHistory() {
        viewModelScope.launch {
            getAttendanceHistoryUseCase().collect { history ->
                _uiState.update { it.copy(attendanceHistory = history) }
            }
        }
    }

    private fun handleSetOfficeLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSettingLocation = true) }
            val result = setOfficeLocationUseCase()
            result.fold(
                onSuccess = { _ ->
                    _uiState.update {
                        it.copy(
                            isSettingLocation = false,
                            userMessage = "Office Location Saved Successfully!"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isSettingLocation = false,
                            userMessage = error.message ?: "Failed to save office location."
                        )
                    }
                }
            )
        }
    }

    private fun handleMarkAttendance() {
        val officeLocation = _uiState.value.officeLocation ?: run {
            _uiState.update { it.copy(userMessage = "Please set office location first!") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isMarkingAttendance = true) }
            val result = markAttendanceUseCase(officeLocation)
            result.fold(
                onSuccess = { _ ->
                    _uiState.update {
                        it.copy(
                            isMarkingAttendance = false,
                            userMessage = "Attendance Marked Successfully!"
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isMarkingAttendance = false,
                            userMessage = error.message ?: "Failed to mark attendance."
                        )
                    }
                }
            )
        }
    }

    private fun clearUserMessage() {
        _uiState.update { it.copy(userMessage = null) }
    }
}
