package com.geopulse.attendance.ui.attendance.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geopulse.attendance.domain.model.GeofenceStatus
import com.geopulse.attendance.ui.theme.StatusGreen
import com.geopulse.attendance.ui.theme.StatusGreenBg
import com.geopulse.attendance.ui.theme.StatusRed
import com.geopulse.attendance.ui.theme.StatusRedBg
import com.geopulse.attendance.ui.theme.TextLight
import com.geopulse.attendance.ui.theme.TextPrimary
import com.geopulse.attendance.ui.theme.TextSecondary

@Composable
fun DistanceGaugeView(
    geofenceStatus: GeofenceStatus,
    radiusMeters: Double = 50.0,
    modifier: Modifier = Modifier
) {
    val distance = when (geofenceStatus) {
        is GeofenceStatus.InRange -> geofenceStatus.distanceMeters
        is GeofenceStatus.OutOfRange -> geofenceStatus.distanceMeters
        is GeofenceStatus.Unconfigured -> null
    }

    val isInRange = geofenceStatus is GeofenceStatus.InRange
    val isUnconfigured = geofenceStatus is GeofenceStatus.Unconfigured

    val accentColor = when {
        isUnconfigured -> TextLight
        isInRange -> StatusGreen
        else -> StatusRed
    }

    val badgeBgColor = when {
        isUnconfigured -> Color(0xFFF1F5F9)
        isInRange -> StatusGreenBg
        else -> StatusRedBg
    }

    val badgeText = when {
        isUnconfigured -> "NOT SET"
        isInRange -> "IN RANGE"
        else -> "OUT OF RANGE"
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gauge Circle
        Box(
            modifier = Modifier.size(130.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(130.dp)) {
                // Background Track
                drawCircle(
                    color = Color(0xFFF1F5F9),
                    radius = size.minDimension / 2 - 8.dp.toPx(),
                    style = Stroke(width = 8.dp.toPx())
                )

                // Progress Indicator Arc
                val sweepAngle = if (distance != null) {
                    val progress = (1.0 - (distance / (radiusMeters * 4))).coerceIn(0.05, 1.0)
                    (progress * 360).toFloat()
                } else {
                    0f
                }

                drawArc(
                    color = accentColor,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (distance != null) "${distance.toInt()}m" else "--",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "AWAY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextLight
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Status Badge Pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(badgeBgColor)
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = accentColor,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Move within 50 meters of the designated office location\nto enable check-in.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 11.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        )
    }
}
