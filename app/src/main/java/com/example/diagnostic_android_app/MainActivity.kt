package com.example.diagnostic_android_app

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramzmania.speedometercomposeview.SpeedometerComposeView
import com.ramzmania.speedometercomposeview.Mode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Force landscape orientation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContent {
            MaterialTheme {
                DashboardGauge()
            }
        }
    }
}

@Composable
fun DashboardGauge() {
    // Mutable states for each gauge's live value
    var speed by remember { mutableStateOf(60f) }
    var rpm by remember { mutableStateOf(2000f) }
    var oilTemp by remember { mutableStateOf(90f) }
    var tirePressure by remember { mutableStateOf(32f) }
    var massAirFlow by remember { mutableStateOf(5f) }

    // Horizontal scroll
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1F2E))
            .padding(16.dp)
            .horizontalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // First Row: Speed, RPM, Oil Temp
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GaugeItem("Speed", 220, speed) { newValue -> speed = newValue }
            GaugeItem("RPM", 8000, rpm) { newValue -> rpm = newValue }
            GaugeItem("Oil Temp", 150, oilTemp) { newValue -> oilTemp = newValue }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Second Row: Tire Pressure, Mass Air Flow
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GaugeItem("Tire Pressure", 50, tirePressure) { newValue -> tirePressure = newValue }
            GaugeItem("Mass Air Flow", 20, massAirFlow) { newValue -> massAirFlow = newValue }
        }
    }
}

@Composable
fun GaugeItem(
    label: String,
    maxRange: Int,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .width(220.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            SpeedometerComposeView(
                speedMeterMaxRange = maxRange,
                currentSpeedValue = value.toInt(),
                needleColor = Color(0xFF00BFFF),
                speedTextColor = Color.White,
                movingSpeedTextColor = Color.White,
                arcWidth = 20f,
                speedometerMode = Mode.GLOW,
                glowMulticolor = true,
                glowSingleColor = when (label) {
                    "Oil Temp" -> Color.Yellow
                    "Tire Pressure" -> Color.Green
                    "Mass Air Flow" -> Color.Magenta
                    else -> Color.Red
                },
                glowRadius = 30f,
                glowSpeedPoints = true,
                baseArcColorConstant = Color(0xFF1A1A1A)
            )
        }
    }
}
