package com.example.gauges

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
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
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.*
import kotlinx.coroutines.delay
import java.lang.Math.toRadians

@Composable
fun DualGauge(
    modifier: Modifier = Modifier,
    initialTachometerValue: Float = 0f, // Initial RPM value (0 to 85)
    initialSpeedometerValue: Float = 0f // Initial MPH value (0 to 220)
) {
    val ferrariLogo = ImageBitmap.imageResource(R.drawable.laferrari) // Ensure you have this resource
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

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tachometer
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Canvas(modifier = Modifier.size(300.dp)) {
                drawTachometer(ferrariLogo, tachometerValue)
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
                drawSpeedometer(speedometerValue)
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

fun DrawScope.drawTachometer(ferrariLogo: ImageBitmap, value: Float) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension / 2f - 20f
    val rimWidth = 20f
    val startAngle = 135f
    val sweepAngle = 270f
    val maxValue = 85f

    // Draw base arc
    drawArc(
        color = Color.White,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = rimWidth),
        size = Size(radius * 2, radius * 2),
        topLeft = Offset(center.x - radius, center.y - radius)
    )

    // Draw red zones (70-85)
    drawArc(
        color = Color.Red,
        startAngle = startAngle + (70f / maxValue) * sweepAngle,
        sweepAngle = (15f / maxValue) * sweepAngle,
        useCenter = false,
        style = Stroke(width = rimWidth),
        size = Size(radius * 2, radius * 2),
        topLeft = Offset(center.x - radius, center.y - radius)
    )

    // Draw tick marks and labels
    for (i in 0..85) {
        val angle = startAngle + (i.toFloat() / maxValue) * sweepAngle
        val rad = toRadians(angle.toDouble()).toFloat()
        val isMajor = i % 10 == 0
        val tickLength = if (isMajor) 25f else 15f
        val start = center + Offset(
            (radius - tickLength) * cos(rad),
            (radius - tickLength) * sin(rad)
        )
        val end = center + Offset(radius * cos(rad), radius * sin(rad))
        drawLine(
            color = Color.White,
            start = start,
            end = end,
            strokeWidth = if (isMajor) 3f else 1f,
            cap = StrokeCap.Round
        )
        if (isMajor) {
            val label = i.toString()
            val textRadius = radius - 40f
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

    // Draw needle
    val needleAngle = startAngle + (value.coerceIn(0f, maxValue) / maxValue) * sweepAngle
    val needleRad = toRadians(needleAngle.toDouble()).toFloat()
    val needleLength = radius * 0.85f
    val needleEnd = center + Offset(needleLength * cos(needleRad), needleLength * sin(needleRad))
    drawLine(
        color = Color.Red,
        start = center,
        end = needleEnd,
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )
    drawCircle(
        color = Color.White,
        radius = 8f,
        center = center
    )

    // Draw Ferrari logo
    val logoSize = 40f
    val logoPos = center + Offset(0f, -radius + 50f)
    drawImage(
        image = ferrariLogo,
        dstOffset = IntOffset(
            (logoPos.x - logoSize / 2f).toInt(),
            (logoPos.y - logoSize / 2f).toInt()
        ),
        dstSize = IntSize(logoSize.toInt(), logoSize.toInt())
    )
}

fun DrawScope.drawSpeedometer(value: Float) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension / 2f - 20f
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
        size = Size(radius * 2, radius * 2),
        topLeft = Offset(center.x - radius, center.y - radius)
    )

    // Draw red zone (180-220)
    drawArc(
        color = Color.Red,
        startAngle = startAngle + (180f / maxValue) * sweepAngle,
        sweepAngle = (40f / maxValue) * sweepAngle,
        useCenter = false,
        style = Stroke(width = rimWidth),
        size = Size(radius * 2, radius * 2),
        topLeft = Offset(center.x - radius, center.y - radius)
    )

    // Draw tick marks and labels
    for (i in 0..220 step 2) {
        val angle = startAngle + (i.toFloat() / maxValue) * sweepAngle
        val rad = toRadians(angle.toDouble()).toFloat()
        val isMajor = i % 20 == 0
        val tickLength = if (isMajor) 25f else 15f
        val start = center + Offset(
            (radius - tickLength) * cos(rad),
            (radius - tickLength) * sin(rad)
        )
        val end = center + Offset(radius * cos(rad), radius * sin(rad))
        drawLine(
            color = Color.White,
            start = start,
            end = end,
            strokeWidth = if (isMajor) 3f else 1f,
            cap = StrokeCap.Round
        )
        if (isMajor) {
            val label = i.toString()
            val textRadius = radius - 40f
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

    // Draw needle
    val needleAngle = startAngle + (value.coerceIn(0f, maxValue) / maxValue) * sweepAngle
    val needleRad = toRadians(needleAngle.toDouble()).toFloat()
    val needleLength = radius * 0.85f
    val needleEnd = center + Offset(needleLength * cos(needleRad), needleLength * sin(needleRad))
    drawLine(
        color = Color.Red,
        start = center,
        end = needleEnd,
        strokeWidth = 4f,
        cap = StrokeCap.Round
    )
    drawCircle(
        color = Color.White,
        radius = 8f,
        center = center
    )
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun DualGaugePreview() {
    DualGauge()
}