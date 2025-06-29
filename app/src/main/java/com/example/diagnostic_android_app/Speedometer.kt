package com.example.diagnostic_android_app
import android.R.attr.textSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.io.path.Path
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Speedometer(
    modifier: Modifier = Modifier,
    speed: Float = 0f, // current speed
    maxSpeed: Float = 220f
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val radius = size.minDimension / 2.2f
        val center = Offset(canvasWidth / 2, canvasHeight / 2)

        // Draw outer arc
        drawArc(
            color = Color.Gray,
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = 8f)
        )

        // Draw tick marks
        for (i in 0..11) {
            val angle = Math.toRadians((135 + i * 27).toDouble())
            val start = Offset(
                x = center.x + (radius - 10) * cos(angle).toFloat(),
                y = center.y + (radius - 10) * sin(angle).toFloat()
            )
            val end = Offset(
                x = center.x + radius * cos(angle).toFloat(),
                y = center.y + radius * sin(angle).toFloat()
            )
            drawLine(Color.White, start, end, strokeWidth = 4f)

            // Draw labels
            val label = (i * 20).toString()
            val labelAngle = angle
            val labelOffset = Offset(
                x = center.x + (radius - 30) * cos(labelAngle).toFloat(),
                y = center.y + (radius - 30) * sin(labelAngle).toFloat()
            )
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    label,
                    labelOffset.x,
                    labelOffset.y,
                    Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 30f
                        textAlign= Paint.Align.CENTER
                    }
                )
            }
        }

        // Draw needle
        val needleAngle = 135f + (speed / maxSpeed) * 270f
        val needleRadians = Math.toRadians(needleAngle.toDouble())
        val needleEnd = Offset(
            x = center.x + (radius - 40) * cos(needleRadians).toFloat(),
            y = center.y + (radius - 40) * sin(needleRadians).toFloat()
        )
        drawLine(Color.Red, center, needleEnd, strokeWidth = 6f)
    }
}

@Preview(showBackground = true)
@Composable
fun SpeedometerPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                 .fillMaxSize()
            .background(Color.Black),
             horizontalArrangement = Arrangement.SpaceEvenly,
//             verticalAlignment = Alignment.CenterVertically

            ) {
                Speedometer(
                    modifier = Modifier.size(300.dp),
                    speed = 120f // Example speed
                )
            }
        }
    }
}

