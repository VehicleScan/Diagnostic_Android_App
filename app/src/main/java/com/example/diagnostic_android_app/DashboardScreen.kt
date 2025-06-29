package com.example.diagnostic_android_app

import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diagnostic_android_app.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun DualGauge(
    modifier: Modifier = Modifier,
    initialTachometerValue: Float = 0f, // Initial RPM value (0 to 85)
    initialSpeedometerValue: Float = 0f // Initial MPH value (0 to 220)
) {
    var tachometerValue by remember { mutableStateOf(initialTachometerValue) }
    var speedometerValue by remember { mutableStateOf(initialSpeedometerValue) }

    // Simulate real-time data updates
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L) // Update every second
            tachometerValue = (0..85).random().toFloat() // Random RPM value
            speedometerValue = (0..220).random().toFloat() // Random MPH value
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tachometer
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Canvas(modifier = Modifier.size(300.dp)) {
                    drawTachometer(value = tachometerValue)
                }
                Text(
                    text = "rpm x100",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = "Shift Indicator",
                    tint = Color.Yellow,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Speedometer
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Canvas(modifier = Modifier.size(300.dp)) {
                    drawSpeedometer(value = speedometerValue)
                }
                Text(
                    text = "mph",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun DrawScope.drawTachometer(value: Float) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val outerRadius = size.minDimension / 2f - 20f
    val innerRadius = outerRadius * 0.7f
    val rimWidth = 20f
    val startAngle = 135f
    val sweepAngle = 270f
    val maxValue = 85f

    // Draw base arc (outer ring)
    drawArc(
        color = Color.White,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = rimWidth),
        size = Size(outerRadius * 2, outerRadius * 2),
        topLeft = Offset(center.x - outerRadius, center.y - outerRadius)
    )

    // Draw red zone (60-85) - outer ring
    drawArc(
        color = Color.Red,
        startAngle = startAngle + (60f / maxValue) * sweepAngle,
        sweepAngle = (25f / maxValue) * sweepAngle,
        useCenter = false,
        style = Stroke(width = rimWidth),
        size = Size(outerRadius * 2, outerRadius * 2),
        topLeft = Offset(center.x - outerRadius, center.y - outerRadius)
    )

    // Draw inner ring
    drawArc(
        color = Color.DarkGray.copy(alpha = 0.7f),
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = 15f),
        size = Size(innerRadius * 2, innerRadius * 2),
        topLeft = Offset(center.x - innerRadius, center.y - innerRadius)
    )

    // Draw outer ring tick marks and labels
    val outerLabels = listOf(5, 10, 20, 25, 30, 35, 40, 45, 50, 55, 60)
    for (i in outerLabels) {
        val angle = startAngle + (i.toFloat() / maxValue) * sweepAngle
        val rad = Math.toRadians(angle.toDouble()).toFloat()
        val isMajor = i % 5 == 0
        val tickLength = if (isMajor) 25f else 15f

        // Outer tick
        val startOuter = center + Offset(
            (outerRadius - tickLength) * cos(rad),
            (outerRadius - tickLength) * sin(rad)
        )
        val endOuter = center + Offset(outerRadius * cos(rad), outerRadius * sin(rad))
        drawLine(
            color = Color.White,
            start = startOuter,
            end = endOuter,
            strokeWidth = if (isMajor) 3f else 1f,
            cap = StrokeCap.Round
        )

        // Draw outer label
        if (isMajor) {
            val label = i.toString()
            val textRadius = outerRadius - 40f
            val textPos = center + Offset(textRadius * cos(rad), textRadius * sin(rad))
            drawIntoCanvas { canvas ->
                val paint = Paint().asFrameworkPaint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 20f
                    textAlign = android.graphics.Paint.Align.CENTER
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.nativeCanvas.drawText(label, textPos.x, textPos.y + 6f, paint)
            }
        }
    }

    // Draw inner ring labels
    val innerLabels = listOf(90, 95, 100, 160, 140, 120, 100, 80, 60, 40, 20)
    for (i in innerLabels) {
        val normalizedValue = i.coerceIn(0, maxValue.toInt())
        val angle = startAngle + (normalizedValue / maxValue) * sweepAngle
        val rad = Math.toRadians(angle.toDouble()).toFloat()

        // Inner label
        val label = i.toString()
        val textRadius = innerRadius - 25f
        val textPos = center + Offset(textRadius * cos(rad), textRadius * sin(rad))
        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint().apply {
                color = if (i >= 90) android.graphics.Color.YELLOW else android.graphics.Color.WHITE
                textSize = 14f
                textAlign = android.graphics.Paint.Align.CENTER
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }
            canvas.nativeCanvas.drawText(label, textPos.x, textPos.y + 6f, paint)
        }
    }

    // Draw needle
    val needleAngle = startAngle + (value.coerceIn(0f, maxValue) / maxValue) * sweepAngle
    val needleRad = Math.toRadians(needleAngle.toDouble()).toFloat()
    val needleLength = outerRadius * 0.85f
    val needleEnd = center + Offset(needleLength * cos(needleRad), needleLength * sin(needleRad))
    drawLine(
        color = Color.Red,
        start = center,
        end = needleEnd,
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )

    // Draw center circle
    drawCircle(
        color = Color.White,
        radius = 8f,
        center = center
    )
}

fun DrawScope.drawSpeedometer(value: Float) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val outerRadius = size.minDimension / 2f - 20f
    val rimWidth = 20f
    val startAngle = 135f
    val sweepAngle = 270f
    val maxValue = 220f

    // Draw base arc
    drawArc(
        color = Color.White,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = rimWidth),
        size = Size(outerRadius * 2, outerRadius * 2),
        topLeft = Offset(center.x - outerRadius, center.y - outerRadius)
    )

    // Draw red zone (180-220)
    drawArc(
        color = Color.Red,
        startAngle = startAngle + (180f / maxValue) * sweepAngle,
        sweepAngle = (40f / maxValue) * sweepAngle,
        useCenter = false,
        style = Stroke(width = rimWidth),
        size = Size(outerRadius * 2, outerRadius * 2),
        topLeft = Offset(center.x - outerRadius, center.y - outerRadius)
    )

    // Draw tick marks and labels
    val labels = listOf(20, 40, 60, 80, 100, 120, 140, 160, 180, 200, 220)
    for (i in labels) {
        val normalizedValue = i.coerceIn(0, maxValue.toInt())
        val angle = startAngle + (normalizedValue / maxValue) * sweepAngle
        val rad = Math.toRadians(angle.toDouble()).toFloat()
        val isMajor = i % 20 == 0
        val tickLength = if (isMajor) 25f else 15f

        // Main tick
        val start = center + Offset(
            (outerRadius - tickLength) * cos(rad),
            (outerRadius - tickLength) * sin(rad)
        )
        val end = center + Offset(outerRadius * cos(rad), outerRadius * sin(rad))
        drawLine(
            color = Color.White,
            start = start,
            end = end,
            strokeWidth = if (isMajor) 3f else 1f,
            cap = StrokeCap.Round
        )

        // Draw label
        if (isMajor) {
            val label = i.toString()
            val textRadius = outerRadius - 40f
            val textPos = center + Offset(textRadius * cos(rad), textRadius * sin(rad))
            drawIntoCanvas { canvas ->
                val paint = Paint().asFrameworkPaint().apply {
                    color = if (i >= 180) android.graphics.Color.RED else android.graphics.Color.WHITE
                    textSize = 20f
                    textAlign = android.graphics.Paint.Align.CENTER
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                canvas.nativeCanvas.drawText(label, textPos.x, textPos.y + 6f, paint)
            }
        }
    }

    // Draw needle
    val needleAngle = startAngle + (value.coerceIn(0f, maxValue) / maxValue) * sweepAngle
    val needleRad = Math.toRadians(needleAngle.toDouble()).toFloat()
    val needleLength = outerRadius * 0.85f
    val needleEnd = center + Offset(needleLength * cos(needleRad), needleLength * sin(needleRad))
    drawLine(
        color = Color.Red,
        start = center,
        end = needleEnd,
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )

    // Draw center circle
    drawCircle(
        color = Color.White,
        radius = 8f,
        center = center
    )
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun DualGaugePreview() {
    DualGauge(
        initialTachometerValue = 45f,
        initialSpeedometerValue = 180f
    )
}

// Extension function for Kotlin math
fun Math.toRadians(degrees: Double): Double = degrees * PI / 180