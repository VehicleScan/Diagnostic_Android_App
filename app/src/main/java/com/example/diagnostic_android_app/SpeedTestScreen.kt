package com.example.diagnostic_android_app

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diagnostic_android_app.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.roundToInt
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

    LaunchedEffect(Unit) {
        coroutineScope.launch { startAnimation(animation1) }
        coroutineScope.launch { startAnimation(animation2) }
    }

    val state1 = animation1.toUiState(maxSpeed = 0f)
    val state2 = animation2.toUiState(maxSpeed = 0f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradient)
    ) {
        item {
            SpeedTestScreenHorizontal(state1, state2)
        }
        item {
            DiagnosticRow()
        }
    }
}


@Composable
fun SpeedTestScreenHorizontal(state1: UiState, state2: UiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularSpeedIndicator(state1.arcValue, 240f)
            SpeedValue(state1.speed)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularSpeedIndicator(state2.arcValue, 240f)
            SpeedValue(state2.speed)
        }
    }
}

@Composable
fun SpeedValue(value: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Speed", style = MaterialTheme.typography.caption)
        Text(
            text = value,
            fontSize = 30.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text("mbps", style = MaterialTheme.typography.caption)
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
fun NavigationView(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        R.drawable.speed2,
        R.drawable.msg2
    )

    BottomNavigation(backgroundColor = DarkColor) {
        items.mapIndexed { index, item ->
            BottomNavigationItem(
                selected = index == selectedItem,
                onClick = { onItemSelected(index) },
                selectedContentColor = BlueSoftColor,
                unselectedContentColor = Color.LightGray,
                icon = {
                    Icon(painterResource(id = item), contentDescription = null)
                }
            )
        }
    }
}

@Composable
fun CircularSpeedIndicator(value: Float, angle: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        drawLines(value, angle)
        drawArcs(value, angle)
    }
}

fun DrawScope.drawArcs(progress: Float, maxValue: Float) {
    val startAngle = 270 - maxValue / 2
    val sweepAngle = maxValue * progress
    val topLeft = Offset(50f, 50f)
    val size = Size(size.width - 100f, size.height - 100f)

    for (i in 0..20) {
        drawArc(
            color = BlueSoftColor.copy(alpha = i / 900f),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 80f + (20 - i) * 20, cap = StrokeCap.Round)
        )
    }

    drawArc(
        color = BlueSoftColor,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = topLeft,
        size = size,
        style = Stroke(width = 86f, cap = StrokeCap.Round)
    )

    drawArc(
        brush = BlueGradient,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = topLeft,
        size = size,
        style = Stroke(width = 80f, cap = StrokeCap.Round)
    )
}

fun DrawScope.drawLines(progress: Float, maxValue: Float, numberOfLines: Int = 40) {
    val oneRotation = maxValue / numberOfLines
    val startValue = if (progress == 0f) 0 else floor(progress * numberOfLines).toInt() + 1

    for (i in startValue..numberOfLines) {
        rotate(i * oneRotation + (180 - maxValue) / 2) {
            drawLine(
                LightColor,
                Offset(if (i % 5 == 0) 80f else 30f, size.height / 2),
                Offset(0f, size.height / 2),
                8f,
                StrokeCap.Round
            )
        }
    }
}

suspend fun updateData(animation: Animatable<Float, AnimationVector1D>, maxSpeed: MutableState<Float>) {
    animation.animateTo(0.9f, keyframes {
        durationMillis = 5000
        0f at 0
        0.65f at 1000
        0.75f at 2000
        0.85f at 3000
        0.9f at 4000 with LinearOutSlowInEasing
    })
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
