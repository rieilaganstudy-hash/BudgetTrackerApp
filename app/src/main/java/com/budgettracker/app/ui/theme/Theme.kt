package com.budgettracker.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF3F8CFF),
    secondary = androidx.compose.ui.graphics.Color(0xFF7ED9B2),
    background = androidx.compose.ui.graphics.Color(0xFFF6F8FC),
    surface = androidx.compose.ui.graphics.Color.White,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.Black,
    onBackground = androidx.compose.ui.graphics.Color(0xFF1F2937),
    onSurface = androidx.compose.ui.graphics.Color(0xFF1F2937)
)

@Composable
fun BudgetTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
