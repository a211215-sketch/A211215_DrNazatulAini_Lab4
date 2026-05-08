package com.example.a211215_drnazatulaini_lab4.model

data class FoodData(
    val name: String,
    val details: String,
    val icon: String,
    val fat: Int = 0,
    val carbs: Int = 0,
    val protein: Int = 0,
    val fiber: Int = 0,
    val sodium: Int = 0,
    val sugar: Int = 0
)