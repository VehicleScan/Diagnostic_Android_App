// UDSActivity.kt
package com.example.diagnostic_android_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

// A simple data model
data class UdsItem(val id: Int, val name: String, val details: String)

class UDSActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                UdsNavHost()
            }
        }
    }
}

@Composable
fun UdsNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list") {
        composable("list") {
            UdsListScreen(
                items = listOf(
                    UdsItem(1, "Item One", "Details for item one…"),
                    UdsItem(2, "Item Two", "Here are the details for item two…"),
                    UdsItem(3, "Item Three", "More info about item three…")
                ),
                onItemClick = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }
        composable(
            "detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStack ->
            val itemId = backStack.arguments!!.getInt("itemId")
            val item = remember { // lookup from your data source
                listOf(
                    UdsItem(1, "Item One", "Details for item one…"),
                    UdsItem(2, "Item Two", "Here are the details for item two…"),
                    UdsItem(3, "Item Three", "More info about item three…")
                ).first { it.id == itemId }
            }
            UdsDetailScreen(item)
        }
    }
}
