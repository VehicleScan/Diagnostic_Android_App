// UdsDetailScreen.kt
package com.example.diagnostic_android_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun UdsDetailScreen(item: UdsItem, navController: NavController? = null) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = item.name, style = MaterialTheme.typography.headlineLarge)
        Text(text = item.details, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Button(onClick = { navController?.navigateUp() }) {
            Text("Back")
        }
    }
}

@Preview(showBackground = true, widthDp = 1000, heightDp = 600)
@Composable
fun UdsDetailScreenPreview() {
    MaterialTheme {
        UdsDetailScreen(UdsItem(0,"name","details"))
    }
}
