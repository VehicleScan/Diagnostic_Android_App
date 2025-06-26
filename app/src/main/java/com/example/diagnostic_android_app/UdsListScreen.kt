package com.example.diagnostic_android_app

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext

@Composable
fun UdsListScreen(items: List<UdsItem>, onItemClick: (Int) -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1F2E))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "UDS Items",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        items.forEach { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
                    .clickable { onItemClick(item.id) }
            ) {
                Text(text = item.name, color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Box(contentAlignment = Alignment.Center){
            Button(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                (context as? UDSActivity)?.finish()
            }) {
                Text("Back", fontSize = 16.sp)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun UdsListScreenPreview() {
    val sampleItems = listOf(
        UdsItem(1, "Speed Sensor", "Monitors the speed of the vehicle."),
        UdsItem(2, "Oil Temp Sensor", "Tracks engine oil temperature."),
        UdsItem(3, "MAF Sensor", "Measures mass air flow into the engine.")
    )
    MaterialTheme {
        UdsListScreen(items = sampleItems, onItemClick = {})
    }
}