// app/src/main/java/com/example/diagnostic_android_app/MainActivity.kt
package com.example.diagnostic_android_app

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.diagnostic_android_app.ui.theme.ComposeSpeedTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContent {
            ComposeSpeedTestTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    SpeedTestScreen()
                }
            }
        }
    }
}