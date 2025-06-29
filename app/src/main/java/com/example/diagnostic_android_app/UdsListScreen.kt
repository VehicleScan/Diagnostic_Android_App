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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import com.example.diagnostic_android_app.ui.theme.BlueSoftColor
import com.example.diagnostic_android_app.ui.theme.ComposeSpeedTestTheme
import com.example.diagnostic_android_app.ui.theme.DarkColor


@Composable
fun UdsListScreen(
    items: List<UdsItem>,
    onItemClick: (Int) -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(DarkColor)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "UDS Items",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(items) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(item.id) },
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = item.iconRes),
                                contentDescription = "${item.name} icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 12.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun UdsListScreenPreview() {
    val sampleItems = listOf(
        UdsItem(1, "Speed Sensor",    "Monitors speed",    R.drawable.carspeed1),
        UdsItem(2, "Oil Temp Sensor", "Tracks oil temp",   R.drawable.thermometer1),
        UdsItem(3, "MAF Sensor",      "Measures airflow",  R.drawable.airflow1)
    )
    ComposeSpeedTestTheme {
        UdsListScreen(items = sampleItems, onItemClick = {})
    }
}



