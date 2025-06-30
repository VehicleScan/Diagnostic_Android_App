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
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.diagnostic_android_app.ui.theme.ComposeSpeedTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        hideSystemBars() // ← FIXED: called inside Activity context

        setContent {
            ComposeSpeedTestTheme {
                MainNavigation()
            }
        }
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
                        0 -> navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
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
            // 1) Dashboard
            composable("home") {
                DashboardScreen2()
            }

            // 2) UDS list
            composable("uds_list") {
                val items = listOf(
                    UdsItem(1, "Speed Sensor",    "Monitors speed",    R.drawable.carspeed1),
                    UdsItem(2, "Oil Temp Sensor", "Tracks oil temp",   R.drawable.thermometer1),
                    UdsItem(3, "MAF Sensor",      "Measures airflow",  R.drawable.airflow1)
                )
                UdsListScreen(items) { selectedId ->
                    navController.navigate("uds_detail/$selectedId")
                }
            }

            // 3) UDS detail
            composable(
                route = "uds_detail/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments!!.getInt("itemId")
                val item = listOf(
                    UdsItem(1, "Speed Sensor",    "Monitors speed",    R.drawable.carspeed1),
                    UdsItem(2, "Oil Temp Sensor", "Tracks oil temp",   R.drawable.thermometer1),
                    UdsItem(3, "MAF Sensor",      "Measures airflow",  R.drawable.airflow1)
                ).first { it.id == itemId }

                UdsDetailScreen(item) {
                    navController.popBackStack()
                }
            }
        }
    }
}
