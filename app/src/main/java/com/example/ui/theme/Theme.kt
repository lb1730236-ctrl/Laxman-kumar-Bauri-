package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SoulSyncDarkColorScheme = darkColorScheme(
    primary = AmethystViolet,
    onPrimary = TextPrimary,
    primaryContainer = DeepVioletContainer,
    onPrimaryContainer = VividViolet,
    secondary = RoseGold,
    onSecondary = SoulObsidian,
    secondaryContainer = RoseContainer,
    onSecondaryContainer = WarmRose,
    tertiary = CelestialIndigo,
    onTertiary = TextPrimary,
    background = SoulObsidian,
    onBackground = TextPrimary,
    surface = SoulSurface,
    onSurface = TextPrimary,
    surfaceVariant = SoulSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = SoulBorder,
    error = EndCallRed,
    onError = TextPrimary
)

@Composable
fun SoulSyncTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SoulSyncDarkColorScheme,
        typography = Typography,
        content = content
    )
}

// Backward-compatible alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    SoulSyncTheme(content = content)
}
