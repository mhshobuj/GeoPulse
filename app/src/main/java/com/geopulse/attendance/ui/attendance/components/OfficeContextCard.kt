package com.geopulse.attendance.ui.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geopulse.attendance.domain.model.LocationCoordinates
import com.geopulse.attendance.ui.theme.BlueAccent
import com.geopulse.attendance.ui.theme.CardBorder
import com.geopulse.attendance.ui.theme.NavyPrimary
import com.geopulse.attendance.ui.theme.TextPrimary
import com.geopulse.attendance.ui.theme.TextSecondary
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun OfficeContextCard(
    officeLocation: LocationCoordinates?,
    isSettingLocation: Boolean,
    onSetOfficeLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val officeLatLng = remember(officeLocation) {
        if (officeLocation != null) {
            LatLng(officeLocation.latitude, officeLocation.longitude)
        } else {
            LatLng(23.7907, 90.3671)
        }
    }

    val cameraPositionState = rememberCameraPositionState(key = officeLatLng.toString()) {
        position = CameraPosition.fromLatLngZoom(officeLatLng, 16f)
    }

    val markerState = remember(officeLatLng) {
        MarkerState(position = officeLatLng)
    }

    LaunchedEffect(officeLatLng) {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(officeLatLng, 16f)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STEP 1: OFFICE CONTEXT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(BlueAccent)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Google Map Interactive Card Container with Overlay Lat/Lon Pill Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                GoogleMap(
                    modifier = Modifier.matchParentSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = false,
                        compassEnabled = false,
                        mapToolbarEnabled = false
                    )
                ) {
                    Marker(
                        state = markerState,
                        title = "Office Location"
                    )
                }

                // Centered Lat/Lon Pill Overlay over Map
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.95f))
                        .border(1.dp, CardBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = BlueAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = officeLocation?.formattedLatLon() ?: "Lat: 23.7907, Lon: 90.3671",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "To mark your attendance, ensure your current office location is correctly identified.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = onSetOfficeLocationClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, BlueAccent),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BlueAccent),
                enabled = !isSettingLocation
            ) {
                if (isSettingLocation) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = BlueAccent,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (officeLocation != null) "Update Office Location" else "Set Office Location",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
            }
        }
    }
}
