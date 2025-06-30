package com.example.diagnostic_android_app

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import com.github.anastr.speedviewlib.AwesomeSpeedometer
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.random.Random
import java.lang.Float.max

suspend fun startAnimation(animation: Animatable<Float, AnimationVector1D>) {
    animation.animateTo(0.84f, keyframes {
        durationMillis = 9000
        0f at 0 with CubicBezierEasing(0f, 1.5f, 0.8f, 1f)
        0.72f at 1000 with CubicBezierEasing(0.2f, -1.5f, 0f, 1f)
        0.76f at 2000
        0.78f at 3000
        0.82f at 4000
        0.85f at 5000
        0.89f at 6000
        0.82f at 7500 with LinearOutSlowInEasing
    })
}

fun Animatable<Float, AnimationVector1D>.toUiState(maxSpeed: Float) = UiState(
    arcValue = value,
    speed = "%.1f".format(value * 100),
    ping = if (value > 0.2f) "${(value * 15).roundToInt()} ms" else "-",
    maxSpeed = if (maxSpeed > 0f) "%.1f mbps".format(maxSpeed) else "-",
    inProgress = isRunning
)

@Composable
fun DashboardScreen2() {
    val animation1 = remember { Animatable(0f) }
    val animation2 = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    var speed1 by remember { mutableStateOf(0f) }
    var speed2 by remember { mutableStateOf(0f) }
    val currentSpeed1 by animateFloatAsState(
        targetValue = speed1,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
    )
    val currentSpeed2 by animateFloatAsState(
        targetValue = speed2,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(Unit) {
        coroutineScope.launch { startAnimation(animation1) }
        coroutineScope.launch { startAnimation(animation2) }
        speed1 = animation1.value * 100
        speed2 = animation2.value * 100
    }

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
            SpeedTestScreenHorizontal(currentSpeed1, currentSpeed2)
        }
        item {
            DiagnosticRow()

        }
    }
}

@Composable
fun SpeedTestScreenHorizontal(speed1: Float, speed2: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var speedometer by remember { mutableStateOf<AwesomeSpeedometer?>(null) }
            AndroidView(
                factory = { context ->
                    AwesomeSpeedometer(context).apply {
                        // Initial setup with minimal configuration
                        speedTo(0f, 0) // Set initial speed to avoid immediate gradient update
                    }.also { speedometer = it }
                },
                modifier = Modifier.size(220.dp),
                update = { view ->
                    view.speedTo(speed1.toFloat(), 2000)
                }
            )
            // Apply configurations after view is created
            LaunchedEffect(speedometer) {
                speedometer?.let {
                    it.setSpeedometerColor(Color.Blue.toArgb())
                    it.trianglesColor = DarkColor2.toArgb()
                    it.indicator.width = 15f
                    it.indicator.color = Color.White.toArgb() // Change needle color to white
                    it.speedTo(speed1.toFloat(), 2000) // Ensure speed is set after config
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
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var speedometer2 by remember { mutableStateOf<AwesomeSpeedometer?>(null) }
            AndroidView(
                factory = { context ->
                    AwesomeSpeedometer(context).apply {
                        speedTo(0f, 0) // Initial speed to avoid gradient crash
                    }.also { speedometer2 = it }
                },
                modifier = Modifier.size(220.dp),
                update = { view ->
                    view.speedTo(speed2.toFloat(), 2000)
                }
            )
            LaunchedEffect(speedometer2) {
                speedometer2?.let {
                    it.setSpeedometerColor(Color.Blue.toArgb())
                    it.trianglesColor = DarkColor2.toArgb()
                    it.indicator.width = 15f
                    it.indicator.color = Color.White.toArgb() // Change needle color to white
                    it.speedTo(speed2.toFloat(), 2000) // Ensure speed is set after config
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
fun NavigationView(selectedItem: Int,onItemSelected: (Int) -> Unit) {
    val items = listOf(
        R.drawable.speed2,
        R.drawable.msg2
    )

    BottomNavigation(backgroundColor = DarkColor) {
        items.mapIndexed { index, item ->
            BottomNavigationItem(
                selected = index == selectedItem,
                onClick = { onItemSelected(index) },
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.LightGray,
                icon = {
                    Icon(painterResource(id = item), contentDescription = null)
                }
            )
        }
    }
}

suspend fun updateData(animation: Animatable<Float, AnimationVector1D>, maxSpeed: MutableState<Float>) {
    animation.snapTo(0.9f) // Instant update without animation
    maxSpeed.value = max(maxSpeed.value, animation.value * 100f)
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        Surface {
            DashboardScreen2()
        }
    }
}