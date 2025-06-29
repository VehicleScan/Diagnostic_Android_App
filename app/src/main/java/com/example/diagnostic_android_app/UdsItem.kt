package com.example.diagnostic_android_app

import androidx.annotation.DrawableRes

data class UdsItem(
    val id: Int,
    val name: String,
    val details: String,
    @DrawableRes val iconRes: Int
)

