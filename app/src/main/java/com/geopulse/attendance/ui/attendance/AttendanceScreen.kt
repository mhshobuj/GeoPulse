package com.geopulse.attendance.ui.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geopulse.attendance.domain.model.GeofenceStatus
import com.geopulse.attendance.ui.attendance.components.AttendanceActionButton
import com.geopulse.attendance.ui.attendance.components.AttendanceHistorySection
import com.geopulse.attendance.ui.attendance.components.DistanceGaugeView
import com.geopulse.attendance.ui.attendance.components.OfficeContextCard
import com.geopulse.attendance.ui.common.LocationPermissionHandler
import com.geopulse.attendance.ui.theme.NavyPrimary
import com.geopulse.attendance.ui.theme.SurfaceBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    viewModel: AttendanceViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LocationPermissionHandler(
        onPermissionGranted = { viewModel.onEvent(AttendanceUiEvent.PermissionGranted) },
        onPermissionDenied = { viewModel.onEvent(AttendanceUiEvent.PermissionDenied) }
    )

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onEvent(AttendanceUiEvent.UserMessageDismissed)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Attendance",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyPrimary,
                            fontSize = 20.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceBg)
            )
        },
        containerColor = SurfaceBg
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // STEP 1: Office Context Card
            item {
                OfficeContextCard(
                    officeLocation = uiState.officeLocation,
                    isSettingLocation = uiState.isSettingLocation,
                    onSetOfficeLocationClick = { viewModel.onEvent(AttendanceUiEvent.SetOfficeLocation) }
                )
            }

            // Real-Time Distance Circular Gauge
            item {
                DistanceGaugeView(
                    geofenceStatus = uiState.geofenceStatus,
                    radiusMeters = uiState.geofenceRadiusMeters
                )
            }

            // STEP 2: Attendance Action Button
            item {
                val isMarkingEnabled = uiState.geofenceStatus is GeofenceStatus.InRange
                AttendanceActionButton(
                    isEnabled = isMarkingEnabled,
                    isMarking = uiState.isMarkingAttendance,
                    onMarkAttendanceClick = { viewModel.onEvent(AttendanceUiEvent.MarkAttendance) }
                )
            }

            // Attendance History Section
            item {
                AttendanceHistorySection(records = uiState.attendanceHistory)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
