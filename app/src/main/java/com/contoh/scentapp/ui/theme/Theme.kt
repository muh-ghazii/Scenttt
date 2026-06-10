package com.contoh.scentapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary          = ScentGold,
    background       = ScentBlack,
    surface          = ScentBlack,
    surfaceVariant   = ScentSearchBg,       // search bar, card bg
    onPrimary        = ScentBlack,
    onBackground     = ScentWhite,
    onSurface        = ScentWhite,
    onSurfaceVariant = ScentTextMuted
)

private val LightColorScheme = lightColorScheme(
    primary          = ScentGold,
    background       = Color(0xFFF5F5F5),   // putih keabu-abuan
    surface          = Color(0xFFFFFFFF),
    surfaceVariant   = Color(0xFFE9ECEF),   // search bar, card bg
    onPrimary        = Color(0xFFFFFFFF),
    onBackground     = Color(0xFF121212),   // teks gelap di mode terang
    onSurface        = Color(0xFF121212),
    onSurfaceVariant = Color(0xFF6B6B6B)
)

@Composable
fun ScentAppTheme(
    darkTheme : Boolean = isSystemInDarkTheme(),
    content   : @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}