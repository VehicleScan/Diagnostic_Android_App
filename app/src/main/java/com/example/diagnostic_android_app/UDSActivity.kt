package com.example.diagnostic_android_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

class UDSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                UdsNavHost()
            }
        }
        // Enable the Up button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

@Preview(showBackground = true, widthDp = 1000, heightDp = 600)
@Composable
fun UdsNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            val items = listOf(
                UdsItem(1, "Speed Sensor", "Monitors the speed of the vehicle."),
                UdsItem(2, "Oil Temp Sensor", "Tracks engine oil temperature."),
                UdsItem(3, "MAF Sensor", "Measures mass air flow into the engine.")
            )
            UdsListScreen(items) { selectedId ->
                navController.navigate("detail/$selectedId")
            }
        }
        composable(
            route = "detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            val item = listOf(
                UdsItem(1, "Speed Sensor", "Monitors the speed of the vehicle."),
                UdsItem(2, "Oil Temp Sensor", "Tracks engine oil temperature."),
                UdsItem(3, "MAF Sensor", "Measures mass air flow into the engine.")
            ).firstOrNull { it.id == itemId }

            item?.let { UdsDetailScreen(it, navController) }
        }
    }
}