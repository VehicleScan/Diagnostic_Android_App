// UdsDetailScreen.kt
package com.example.diagnostic_android_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun UdsDetailScreen(item: UdsItem, navController: NavController ?= null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1F2E))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item.name,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = item.details,
            color = Color.LightGray,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { navController?.popBackStack() }) {
            Text("Back", fontSize = 16.sp)
        }
    }
}


@Preview(showBackground = true, widthDp = 1000, heightDp = 600)
@Composable
fun UdsDetailScreenPreview() {
    val fakeNavController = rememberNavController()
    MaterialTheme {
        UdsDetailScreen(
            item = UdsItem(0, "Speed Sensor", "Monitors speed..."),
            navController = fakeNavController
        )
    }
}

