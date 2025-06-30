package com.example.diagnostic_android_app

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.diagnostic_android_app.ui.theme.ComposeSpeedTestTheme
import kotlinx.coroutines.launch

data class SpeedometerConfig(
    val minSpeed: Float = 0f,
    val maxSpeed: Float = 100f,
    val color: Color = Color.Blue
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        hideSystemBars()

        // Restore state if available
        if (savedInstanceState != null) {
            speed1.value = savedInstanceState.getFloat("speed1", 0f)
            speed2.value = savedInstanceState.getFloat("speed2", 0f)
        } else {
            // Initialize values only on first creation
            lifecycleScope.launch {
                updateSpeedometer(1, 84f)
                updateSpeedometer(2, 50f)

            }
        }

        setContent {
            ComposeSpeedTestTheme {
                MainNavigation()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("speed1", speed1.value)
        outState.putFloat("speed2", speed2.value)
    }

    private fun hideSystemBars() {
        if (packageManager.hasSystemFeature("android.hardware.type.automotive")) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }

    // Function to update individual speedometer
    fun updateSpeedometer(index: Int, newSpeed: Float) {
        lifecycleScope.launch {
            when (index) {
                1 -> speed1.value = newSpeed
                2 -> speed2.value = newSpeed
            }
        }
    }

    // Store speedometer states (made accessible for updateSpeedometer)
    companion object {
        val speed1 = mutableStateOf(0f)
        val speed2 = mutableStateOf(0f)
        val config1 = mutableStateOf(SpeedometerConfig())
        val config2 = mutableStateOf(SpeedometerConfig())
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val selectedItem = remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationView(
                selectedItem = selectedItem.value,
                onItemSelected = { index ->
                    selectedItem.value = index
                    when (index) {
                        0 -> {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                        1 -> navController.navigate("uds_list") {
                            popUpTo("uds_list") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") {
                DashboardScreen2(
                    speed1 = MainActivity.speed1.value,
                    speed2 = MainActivity.speed2.value,
                    config1 = MainActivity.config1.value,
                    config2 = MainActivity.config2.value
                )
            }

            composable("uds_list") {
                val items = listOf(
                    UdsItem(1, "Speed Sensor", "Monitors speed", R.drawable.carspeed1),
                    UdsItem(2, "Oil Temp Sensor", "Tracks oil temp", R.drawable.thermometer1),
                    UdsItem(3, "MAF Sensor", "Measures airflow", R.drawable.airflow1)
                )
                UdsListScreen(items) { selectedId ->
                    navController.navigate("uds_detail/$selectedId")
                }
            }

            composable(
                route = "uds_detail/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments!!.getInt("itemId")
                val item = listOf(
                    UdsItem(1, "Speed Sensor", "Monitors speed", R.drawable.carspeed1),
                    UdsItem(2, "Oil Temp Sensor", "Tracks oil temp", R.drawable.thermometer1),
                    UdsItem(3, "MAF Sensor", "Measures airflow", R.drawable.airflow1)
                ).first { it.id == itemId }

                UdsDetailScreen(item) {
                    navController.popBackStack()
                }
            }
        }
    }
}