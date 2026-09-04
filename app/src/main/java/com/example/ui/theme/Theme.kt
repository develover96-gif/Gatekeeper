package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val GatekeeperDarkColorScheme = darkColorScheme(
  primary = PrimaryIndigo,
  onPrimary = OnPrimaryIndigo,
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  secondary = TextSecondary,
  onSecondary = CanvasSubstrate,
  secondaryContainer = SurfaceContainerHighest,
  onSecondaryContainer = TextSecondary,
  tertiary = CalmMoss,
  onTertiary = OnCalmMoss,
  tertiaryContainer = CalmMossContainer,
  onTertiaryContainer = Color(0xFF00311E),
  background = CanvasBase,
  onBackground = TextPrimary,
  surface = CanvasSubstrate,
  onSurface = TextPrimary,
  surfaceVariant = SurfaceContainerHigh,
  onSurfaceVariant = TextSecondary,
  surfaceContainer = SurfaceContainer,
  surfaceContainerLow = SurfaceContainerLow,
  surfaceContainerLowest = SurfaceContainerLowest,
  surfaceContainerHigh = SurfaceContainerHigh,
  surfaceContainerHighest = SurfaceContainerHighest,
  outline = TextMuted,
  outlineVariant = OutlineVariant,
  error = ErrorRed,
  errorContainer = ErrorContainer,
  onError = OnError
)

val GatekeeperLightColorScheme = lightColorScheme(
  primary = Color(0xFF3D4EBA),
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE7E9F7),
  onPrimaryContainer = Color(0xFF001488),
  secondary = Color(0xFF484B58),
  onSecondary = Color.White,
  tertiary = Color(0xFF2E7D5B),
  onTertiary = Color.White,
  background = Color(0xFFFFFFFF),
  onBackground = Color(0xFF1B1D23),
  surface = Color(0xFFF1F1EF),
  onSurface = Color(0xFF1B1D23),
  outline = Color(0xFF6B6D76)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to true for Gatekeeper Dark Glass
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) GatekeeperDarkColorScheme else GatekeeperLightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
