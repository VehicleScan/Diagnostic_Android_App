package com.example.diagnostic_android_app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.anastr.speedviewlib.AwesomeSpeedometer
import com.github.anastr.speedviewlib.SpeedView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val rpmGauge: AwesomeSpeedometer = findViewById(R.id.rpmGauge)
        val oilTempGauge: SpeedView = findViewById(R.id.oilTempGauge)
        val tirePressureGauge: SpeedView = findViewById(R.id.tirePressureGauge)

        // Simulated "normal" sensor values
        rpmGauge.speedTo(2200f)
        oilTempGauge.speedTo(95f)
        tirePressureGauge.speedTo(2.5f)
    }
}
