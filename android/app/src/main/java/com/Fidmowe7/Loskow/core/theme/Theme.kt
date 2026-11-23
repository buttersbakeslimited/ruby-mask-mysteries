package com.Fidmowe7.Loskow.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val RubyMaskScheme = darkColorScheme(
    primary = Gold,
    onPrimary = LacquerBlack,
    primaryContainer = VelvetBrown,
    onPrimaryContainer = TextPrimary,
    secondary = Ruby,
    onSecondary = TextPrimary,
    secondaryContainer = SurfaceRaised,
    onSecondaryContainer = TextSecondary,
    tertiary = GoldLight,
    onTertiary = LacquerBlack,
    background = LacquerBlack,
    onBackground = TextPrimary,
    surface = SurfaceDeep,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = HairlineGold,
    error = Ruby,
    onError = TextPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RubyMaskScheme,
        typography = AppTypography,
        content = content
    )
}
