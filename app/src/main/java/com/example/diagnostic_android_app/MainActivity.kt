package com.example.diagnostic_android_app

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1F2E)) // Dark background inspired by design2.jpeg
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Engine RPM Speedometer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Engine RPM",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier.size(200.dp), // Control size via parent Box
                contentAlignment = Alignment.Center
            ) {
                SpeedometerComposeView(
                    speedMeterMaxRange = 8000,
                    currentSpeedValue = 2000,
                    needleColor = Color(0xFF00BFFF), // Blue for glowing effect
                    speedTextColor = Color.White,
                    movingSpeedTextColor = Color.White,
                    arcWidth = 20f,
                    speedometerMode = Mode.GLOW,
                    glowMulticolor = true,
                    glowSingleColor = Color.Red, // Red for high RPM zone
                    glowRadius = 30f,
                    glowSpeedPoints = true,
                    baseArcColorConstant = Color(0xFF1A1A1A) // Dark gray
                )
            }
        }

        // Oil Temperature Speedometer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Oil Temp",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier.size(200.dp), // Control size via parent Box
                contentAlignment = Alignment.Center
            ) {
                SpeedometerComposeView(
                    speedMeterMaxRange = 150,
                    currentSpeedValue = 90,
                    needleColor = Color(0xFF00BFFF),
                    speedTextColor = Color.White,
                    movingSpeedTextColor = Color.White,
                    arcWidth = 20f,
                    speedometerMode = Mode.GLOW,
                    glowMulticolor = true,
                    glowSingleColor = Color.Yellow, // Yellow for high temp warning
                    glowRadius = 30f,
                    glowSpeedPoints = true,
                    baseArcColorConstant = Color(0xFF1A1A1A)
                )
            }
        }

        // Tire Pressure Speedometer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Tire Pressure",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier.size(200.dp), // Control size via parent Box
                contentAlignment = Alignment.Center
            ) {
                SpeedometerComposeView(
                    speedMeterMaxRange = 50,
                    currentSpeedValue = 32,
                    needleColor = Color(0xFF00BFFF),
                    speedTextColor = Color.White,
                    movingSpeedTextColor = Color.White,
                    arcWidth = 20f,
                    speedometerMode = Mode.GLOW,
                    glowMulticolor = true,
                    glowSingleColor = Color.Green, // Green for normal pressure
                    glowRadius = 30f,
                    glowSpeedPoints = true,
                    baseArcColorConstant = Color(0xFF1A1A1A)
                )
            }
        }
    }
}