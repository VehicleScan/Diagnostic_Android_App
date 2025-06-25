package com.example.diagnostic_android_app

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.BLACK)
        }

        val gauge = GaugeView(this).apply {
            layoutParams = LinearLayout.LayoutParams(600, 600)
            setMaxValue(7000f)
            setUnit("RPM")
            setValue(2200f)
        }

        layout.addView(gauge)
        setContentView(layout)

    }

}
