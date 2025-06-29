package com.example.diagnostic_android_app

// Add this before your Composables
data class UiState(
    val arcValue: Float,
    val speed: String,
    val ping: String,
    val maxSpeed: String,
    val inProgress: Boolean = false
)
