package com.example.diagnostic_android_app

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
import com.ramzmania.speedometercomposeview.Mode
import com.ramzmania.speedometercomposeview.SpeedometerComposeView

@Composable
fun DashboardGauge() {
    var speed by remember { mutableStateOf(60f) }
    var rpm by remember { mutableStateOf(2000f) }
    var oilTemp by remember { mutableStateOf(90f) }
    var tirePressure by remember { mutableStateOf(32f) }
    var massAirFlow by remember { mutableStateOf(5f) }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1F2E))
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GaugeItem("Speed", 220, speed) { speed = it }
            GaugeItem("RPM", 8000, rpm) { rpm = it }
            GaugeItem("Oil Temp", 150, oilTemp) { oilTemp = it }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GaugeItem("Tire Pressure", 50, tirePressure) { tirePressure = it }
            GaugeItem("Mass Air Flow", 20, massAirFlow) { massAirFlow = it }
        }

        Button(
            onClick = {
                val intent = Intent(context, UDSActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BFFF))
        ) {
            Text("Go to Details", color = Color.White, fontSize = 16.sp)
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

@Preview(showBackground = true, widthDp = 1000, heightDp = 600)
@Composable
fun DashboardGaugePreview() {
    MaterialTheme {
        DashboardGauge()
    }
}
