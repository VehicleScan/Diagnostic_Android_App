package com.example.diagnostic_android_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ramzmania.speedometercomposeview.SpeedometerComposeView
import com.ramzmania.speedometercomposeview.Mode
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import java.time.format.TextStyle
import kotlin.math.*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                DashboardGauge()
            }
        }
    }
    @Composable
    fun DashboardGauge() {
        SpeedometerComposeView(
            speedMeterMaxRange = 220,
            currentSpeedValue = 80,
            needleColor = Color.Red,
            speedTextColor = colorResource(id = R.color.white),
            movingSpeedTextColor = Color.White,
            arcWidth = 50f,
            speedometerMode = Mode.GLOW,
            glowMulticolor = false,
            glowSingleColor = Color.Red,
            speedFont = ResourcesCompat.getFont(this, R.font.font_speed),
            speedometerNumberFont = ResourcesCompat.getFont(this, R.font.font_speed_digits),
            glowRadius = 28f,
            glowSpeedPoints = true,
            baseArcColorConstant = Color(0x33FF0000)
        )
    }
}

