// Theme.kt
package com.example.diagnostic_android_app.ui.theme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
private val LightColors = lightColorScheme(
    primary          = AppPrimary,
    onPrimary        = AppOnPrimary,
    primaryContainer = AppPrimaryLight,
    onPrimaryContainer = AppOnPrimary,
    background       = Color.White,
    onBackground     = Color.Black,
    surface          = Color.White,
    onSurface        = Color.Black,
    // …other roles…
)

private val DarkColors = darkColorScheme(
    primary          = AppPrimaryDark,
    onPrimary        = AppOnPrimary,
    primaryContainer = AppPrimary,
    onPrimaryContainer = AppOnPrimary,
    background       = DarkBackground,
    onBackground     = Color.White,
    surface          = DarkBackground,
    onSurface        = Color.White,

)
// Material3 Shapes
val Shapes = Shapes(
    small  = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large  = RoundedCornerShape(16.dp)
)

@Composable
fun ComposeSpeedTestTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography  = Typography,
        shapes      = Shapes,
        content     = content
    )
}
