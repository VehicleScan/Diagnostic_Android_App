package com.example.diagnostic_android_app
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.diagnostic_android_app.ui.theme.ComposeSpeedTestTheme
import kotlinx.coroutines.launch
import kotlin.math.max

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContent {
            ComposeSpeedTestTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
//                    DashboardScreen()
                    MainNavigation()
                }
            }
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
                        1 -> navController.navigate("messages") {
                            popUpTo("messages") { inclusive = true }
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
            composable("home")     { DashboardScreen2() }
            composable("messages") { UDSActivity()    }
        }
    }
}
