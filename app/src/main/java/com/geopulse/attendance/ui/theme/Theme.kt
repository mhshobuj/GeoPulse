package com.geopulse.attendance.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = CardBg,
    secondary = BlueAccent,
    background = SurfaceBg,
    surface = CardBg,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun GeoPulseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
