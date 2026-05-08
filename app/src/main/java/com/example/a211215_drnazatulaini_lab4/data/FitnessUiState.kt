package com.example.a211215_drnazatulaini_lab4.data

import com.example.a211215_drnazatulaini_lab4.model.*
data class FitnessUiState(
    //val selectedDate: String = "Today",
    val loggedMeals: Map<String, List<FoodData>> = emptyMap(),
    val loggedExercises: List<ExerciseData> = emptyList(),
    val selectedMeal: String = "",
    val totalIntake: Int = 0,
    val totalBurned: Int = 0,
    val currentWeight: String = "55.2",
    val targetWeight: String = "45.0",
    val remainingCalories: Int = 2000
)