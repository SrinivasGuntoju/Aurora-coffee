package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AuroraColorScheme = darkColorScheme(
    primary = AuroraGold,
    onPrimary = AuroraBackground,
    primaryContainer = AuroraCardElevated,
    onPrimaryContainer = AuroraCream,
    secondary = AuroraCaramel,
    onSecondary = AuroraBackground,
    secondaryContainer = AuroraCard,
    onSecondaryContainer = AuroraCream,
    tertiary = AuroraAmber,
    onTertiary = AuroraBackground,
    background = AuroraBackground,
    onBackground = AuroraCream,
    surface = AuroraSurface,
    onSurface = AuroraCream,
    surfaceVariant = AuroraSurfaceVariant,
    onSurfaceVariant = AuroraMuted,
    outline = AuroraBorder,
    outlineVariant = AuroraBorderLight
)

@Composable
fun AuroraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AuroraColorScheme,
        typography = Typography,
        content = content
    )
}
