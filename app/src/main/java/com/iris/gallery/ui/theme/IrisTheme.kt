package com.iris.gallery.ui.theme

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.DeviceFontFamilyName
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.iris.gallery.data.AccentColor
import com.iris.gallery.data.ThemeMode
import com.iris.gallery.ui.glass.GlassSettings

val OnyxYellow = Color(0xFFFFD600)
val SolarGold = Color(0xFFFACC15)
val PitchBlack = Color(0xFF000000)
val ObsidianSurface = Color(0xFF0B0B0E)
val ObsidianCard = Color(0xFF121216)
val ObsidianElevated = Color(0xFF1A1A20)
val ObsidianBorder = Color(0xFF24242C)

val ExpressiveShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

private val AppRoundedHeavy = FontFamily(
    Font(DeviceFontFamilyName("sans-serif-rounded"), weight = FontWeight.ExtraBold),
    Font(DeviceFontFamilyName("sans-serif-rounded"), weight = FontWeight.Bold),
    Font(DeviceFontFamilyName("sans-serif"), weight = FontWeight.ExtraBold),
    Font(DeviceFontFamilyName("sans-serif"), weight = FontWeight.Bold),
)

val RoundedHeavy: FontFamily
    get() = if (GlassSettings.useSystemFont) FontFamily.Default else AppRoundedHeavy

/**
 * Exclusive AMOLED Pitch Black & Onyx Yellow color scheme.
 * No other color modes or light themes exist — every surface is pure pitch black (#000000)
 * or deep obsidian (#0B0B0E / #121216) paired with high-contrast Onyx Yellow (#FFD600).
 */
val AmoledPitchBlackYellowScheme: ColorScheme = darkColorScheme(
    primary = OnyxYellow,
    onPrimary = PitchBlack,
    primaryContainer = Color(0xFF2B2300),
    onPrimaryContainer = Color(0xFFFFEA70),
    inversePrimary = Color(0xFF705D00),
    secondary = SolarGold,
    onSecondary = PitchBlack,
    secondaryContainer = Color(0xFF221D08),
    onSecondaryContainer = Color(0xFFFDE047),
    tertiary = Color(0xFFF59E0B),
    onTertiary = PitchBlack,
    tertiaryContainer = Color(0xFF291A04),
    onTertiaryContainer = Color(0xFFFDE68A),
    background = PitchBlack,
    onBackground = Color(0xFFFAFAFA),
    surface = PitchBlack,
    onSurface = Color(0xFFFAFAFA),
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = Color(0xFFB4B4BE),
    surfaceTint = OnyxYellow,
    inverseSurface = Color(0xFFFAFAFA),
    inverseOnSurface = PitchBlack,
    surfaceContainerLowest = PitchBlack,
    surfaceContainerLow = Color(0xFF060608),
    surfaceContainer = ObsidianSurface,
    surfaceContainerHigh = ObsidianCard,
    surfaceContainerHighest = ObsidianElevated,
    outline = Color(0xFF3F3F48),
    outlineVariant = ObsidianBorder,
    error = Color(0xFFFF5252),
    onError = PitchBlack,
    errorContainer = Color(0xFF3B0D0D),
    onErrorContainer = Color(0xFFFFB4AB),
)

@Composable
fun IrisTheme(
    @Suppress("UNUSED_PARAMETER") accentColor: AccentColor = AccentColor.IRIS,
    @Suppress("UNUSED_PARAMETER") themeMode: ThemeMode = ThemeMode.DARK,
    @Suppress("UNUSED_PARAMETER") amoledBlack: Boolean = true,
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val activity = view.context as? ComponentActivity
        SideEffect {
            val window = activity?.window ?: (view.context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
            if (activity != null) {
                val barStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                runCatching {
                    activity.enableEdgeToEdge(statusBarStyle = barStyle, navigationBarStyle = barStyle)
                    insetsController.isAppearanceLightStatusBars = false
                    insetsController.isAppearanceLightNavigationBars = false
                }
            }
        }
        DisposableEffect(Unit) {
            (view.context as? Activity)?.window?.setBackgroundDrawable(ColorDrawable(PitchBlack.toArgb()))
            onDispose { }
        }
    }

    MaterialTheme(
        colorScheme = AmoledPitchBlackYellowScheme,
        shapes = ExpressiveShapes,
        typography = Typography(),
        content = content,
    )
}
