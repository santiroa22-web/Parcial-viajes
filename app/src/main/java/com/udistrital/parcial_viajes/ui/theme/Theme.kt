package com.udistrital.parcial_viajes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LumiColorScheme = lightColorScheme(
    primary = LumiLavender,
    onPrimary = Color.White,
    primaryContainer = LumiLavenderSoft,
    onPrimaryContainer = LumiTextDark,
    secondary = LumiMint,
    onSecondary = LumiTextDark,
    background = LumiCream,
    onBackground = LumiTextDark,
    surface = LumiSurface,
    onSurface = LumiTextDark
)

@Composable
fun Parcial_viajesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LumiColorScheme,
        content = content
    )
}