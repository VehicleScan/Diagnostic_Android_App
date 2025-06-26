package com.example.diagnostic_android_app.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diagnostic_android_app.UDSActivity
import com.ramzmania.speedometercomposeview.Mode
import com.ramzmania.speedometercomposeview.SpeedometerComposeView
import kotlinx.coroutines.delay

@Composable
fun DashboardGauge() {
    var speed by remember { mutableStateOf(60f) }
    var rpm by remember { mutableStateOf(2000f) }
    var oilTemp by remember { mutableStateOf(90f) }
    var tirePressure by remember { mutableStateOf(32f) }
    var massAirFlow by remember { mutableStateOf(5f) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    // Auto-random updates for demo
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            speed = (40..140).random().toFloat()
            rpm = (1000..7000).random().toFloat()
            oilTemp = (70..120).random().toFloat()
            tirePressure = (28..36).random().toFloat()
            massAirFlow = (3..10).random().toFloat()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFF121820)) // darker automotive background
            .padding(WindowInsets.systemBars.asPaddingValues()) // avoid status/nav bar
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row 1: Speed, RPM
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GaugeItem("Speed", 220, speed, "km/h")
            GaugeItem("RPM", 8000, rpm, "RPM")
        }

        // Row 2: Oil Temp, Tire Pressure
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GaugeItem("Oil Temp", 150, oilTemp, "°C")
            GaugeItem("Tire Pressure", 50, tirePressure, "psi")
        }

        // Row 3: Mass Air Flow
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            GaugeItem("Mass Air Flow", 20, massAirFlow, "g/s")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, UDSActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
        ) {
            Text("Go to Details", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
fun GaugeItem(
    label: String,
    maxRange: Int,
    value: Float,
    unit: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .width(340.dp)
    ) {
        Text(
            text = label,
            color = Color.LightGray,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Box(
            modifier = Modifier.size(340.dp),
            contentAlignment = Alignment.Center
        ) {
            SpeedometerComposeView(
                speedMeterMaxRange = maxRange,
                currentSpeedValue = value.toInt(),
                needleColor = Color(0xFF00BFFF),
                speedTextColor = Color.White,
                movingSpeedTextColor = Color.White,
                arcWidth = 32f,
                speedometerMode = Mode.GLOW,
                glowMulticolor = true,
                glowSingleColor = when (label) {
                    "Oil Temp" -> Color.Yellow
                    "Tire Pressure" -> Color.Green
                    "Mass Air Flow" -> Color.Magenta
                    else -> Color.Red
                },
                glowRadius = 42f,
                glowSpeedPoints = true,
                baseArcColorConstant = Color(0xFF2A2A2A)
            )
        }

        Text(
            text = "${value.toInt()} $unit",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true, widthDp = 1000, heightDp = 600)
@Composable
fun DashboardGaugePreview() {
    MaterialTheme {
        DashboardGauge()
    }
}