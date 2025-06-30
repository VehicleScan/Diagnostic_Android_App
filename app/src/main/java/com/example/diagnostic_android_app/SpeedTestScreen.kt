package com.example.diagnostic_android_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.diagnostic_android_app.ui.theme.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.github.anastr.speedometer.AwesomeSpeedometer
import com.github.anastr.speedviewlib.AwesomeSpeedometer

@Composable
fun DashboardScreen2(speed1: Float, speed2: Float, config1: SpeedometerConfig, config2: SpeedometerConfig) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF3533CD), Color(0xFF000000)),
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        item {
            SpeedTestScreenHorizontal(speed1, speed2, config1, config2)
        }
        item {
            DiagnosticRow()
        }
    }
}

@Composable
fun SpeedometerComposable(speed: Float, config: SpeedometerConfig) {
    var speedometer by remember { mutableStateOf<AwesomeSpeedometer?>(null) }
    var lastSpeed by remember { mutableStateOf<Float?>(null) }

    AndroidView(
        factory = { context ->
            AwesomeSpeedometer(context).apply {
                minSpeed = config.minSpeed
                maxSpeed = config.maxSpeed
                speedTo(0f, 0)
            }.also { speedometer = it }
        },
        modifier = Modifier.size(220.dp),
        update = { view ->
            // Only update if speed is new
            if (lastSpeed != speed) {
                lastSpeed = speed
                view.speedTo(speed, 1000) // optional: set duration
            }
        }
    )

    LaunchedEffect(speedometer, config) {
        speedometer?.apply {
            setSpeedometerColor(config.color.toArgb())
            trianglesColor = DarkColor2.toArgb()
            indicator.width = 15f
            indicator.color = Color.White.toArgb()
            minSpeed = config.minSpeed
            maxSpeed = config.maxSpeed
        }
    }
}


@Composable
fun SpeedTestScreenHorizontal(speed1: Float, speed2: Float, config1: SpeedometerConfig, config2: SpeedometerConfig) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var speedometer by remember { mutableStateOf<AwesomeSpeedometer?>(null) }
            AndroidView(
                factory = { context ->
                    AwesomeSpeedometer(context).apply {
                        minSpeed = config1.minSpeed
                        maxSpeed = config1.maxSpeed
                        speedTo(0f, 0)
                    }.also { speedometer = it }
                },
                modifier = Modifier.size(220.dp),
                update = { view ->
                    view.speedTo(speed1, 0)
                }
            )
            LaunchedEffect(speedometer, config1, speed1) {
                speedometer?.let {
                    it.setSpeedometerColor(config1.color.toArgb())
                    it.trianglesColor = DarkColor2.toArgb()
                    it.indicator.width = 15f
                    it.indicator.color = Color.White.toArgb()
                    it.minSpeed = config1.minSpeed
                    it.maxSpeed = config1.maxSpeed
                    it.speedTo(speed1, 0)
                }
            }
        }

        Box(
            modifier = Modifier
                .size(150.dp)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.logo)
                    .crossfade(true)
                    .scale(Scale.FIT)
                    .build(),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var speedometer2 by remember { mutableStateOf<AwesomeSpeedometer?>(null) }
            AndroidView(
                factory = { context ->
                    AwesomeSpeedometer(context).apply {
                        minSpeed = config2.minSpeed
                        maxSpeed = config2.maxSpeed
                        speedTo(0f, 0)
                    }.also { speedometer2 = it }
                },
                modifier = Modifier.size(220.dp),
                update = { view ->
                    view.speedTo(speed2, 0)
                }
            )
            LaunchedEffect(speedometer2, config2, speed2) {
                speedometer2?.let {
                    it.setSpeedometerColor(config2.color.toArgb())
                    it.trianglesColor = DarkColor2.toArgb()
                    it.indicator.width = 15f
                    it.indicator.color = Color.White.toArgb()
                    it.minSpeed = config2.minSpeed
                    it.maxSpeed = config2.maxSpeed
                    it.speedTo(speed2, 0)
                }
            }
        }
    }
}

@Composable
fun DiagnosticRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DiagnosticItem(iconRes = R.drawable.carengine1, label = "Engine", value = "205°F")
        DiagnosticItem(iconRes = R.drawable.battery2, label = "Battery", value = "13.5V")
        DiagnosticItem(iconRes = R.drawable.error1, label = "Error log", value = "P0140")
    }
}

@Composable
fun DiagnosticItem(iconRes: Int, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.caption
        )
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun NavigationView(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf(R.drawable.speed2, R.drawable.msg2)
    BottomNavigation(backgroundColor = DarkColor) {
        items.mapIndexed { index, item ->
            BottomNavigationItem(
                selected = index == selectedItem,
                onClick = { onItemSelected(index) },
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.LightGray,
                icon = { Icon(painterResource(id = item), contentDescription = null) }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Surface {
            DashboardScreen2(
                speed1 = 0f,
                speed2 = 0f,
                config1 = SpeedometerConfig(),
                config2 = SpeedometerConfig()
            )
        }
    }
}