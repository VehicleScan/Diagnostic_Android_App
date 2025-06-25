package com.example.diagnostic_android_app

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.*

class GaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var needleAngle = 0f
    private var currentValue = 0f
    private var maxValue = 100f
    private var unit = "km/h"
    private var gaugeColor = Color.CYAN

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val needlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }

    fun setValue(value: Float) {
        currentValue = value.coerceIn(0f, maxValue)
        needleAngle = mapValueToAngle(currentValue)
        invalidate()
    }

    fun setMaxValue(max: Float) {
        maxValue = max
        invalidate()
    }

    fun setUnit(unitLabel: String) {
        unit = unitLabel
    }

    private fun mapValueToAngle(value: Float): Float {
        val startAngle = 135f
        val sweepAngle = 270f
        return startAngle + (value / maxValue) * sweepAngle
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - 20

        // Draw outer ring
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        paint.color = Color.DKGRAY
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Draw tick marks and labels
        paint.color = gaugeColor
        paint.strokeWidth = 4f
        val tickCount = 10
        for (i in 0..tickCount) {
            val angle = Math.toRadians((135 + (i * 270f / tickCount)).toDouble())
            val inner = radius - 20
            val outer = radius
            val x1 = centerX + cos(angle) * inner
            val y1 = centerY + sin(angle) * inner
            val x2 = centerX + cos(angle) * outer
            val y2 = centerY + sin(angle) * outer
            canvas.drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), paint)

            val label = (i * maxValue / tickCount).toInt().toString()
            val textRadius = radius - 50
            val tx = centerX + cos(angle) * textRadius
            val ty = centerY + sin(angle) * textRadius + 10
            canvas.drawText(label, tx.toFloat(), ty.toFloat(), textPaint)
        }

        // Draw needle
        val angleRad = Math.toRadians(needleAngle.toDouble())
        val needleLength = radius - 40
        val needleX = centerX + cos(angleRad) * needleLength
        val needleY = centerY + sin(angleRad) * needleLength
        canvas.drawLine(centerX, centerY, needleX.toFloat(), needleY.toFloat(), needlePaint)

        // Draw center dot
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
        canvas.drawCircle(centerX, centerY, 12f, paint)

        // Draw current value and unit
        canvas.drawText("$currentValue", centerX, centerY + 100, textPaint)
        canvas.drawText(unit, centerX, centerY + 140, textPaint)
    }
}
