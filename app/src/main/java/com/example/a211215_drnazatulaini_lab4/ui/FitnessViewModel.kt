package com.example.a211215_drnazatulaini_lab4.ui

import androidx.lifecycle.ViewModel
import com.example.a211215_drnazatulaini_lab4.data.FitnessUiState
import com.example.a211215_drnazatulaini_lab4.model.FoodData
import com.example.a211215_drnazatulaini_lab4.model.ExerciseData
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.a211215_drnazatulaini_lab4.data.getCalories
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class FitnessViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FitnessUiState())
    val uiState: StateFlow<FitnessUiState> = _uiState
    fun updateSelectedMeal(meal: String) {
        _uiState.update { it.copy(selectedMeal = meal) }
    }

    fun updateWeight(current: String, target: String) {
        _uiState.update {
            it.copy(currentWeight = current, targetWeight = target)
        }
        calculateTotals()
    }

    private fun calculateTotals() {
        val intake = _uiState.value.loggedMeals.values
            .flatten()
            .sumOf { getCalories(it.details) }

        val weightVal = _uiState.value.currentWeight.toDoubleOrNull() ?: 60.0
        val budget = (weightVal * 30).toInt()

        _uiState.update {
            it.copy(
                totalIntake = intake,
                remainingCalories = budget - intake
            )
        }
    }

    val loggedMeals: Map<String, List<FoodData>>
        get() = uiState.value.loggedMeals

    val loggedExercises: List<ExerciseData>
        get() = uiState.value.loggedExercises

    var selectedMeal: String?
        get() = uiState.value.selectedMeal
        set(value) {
            updateSelectedMeal(value ?: "")
        }

    fun toggleFood(mealKey: String, food: FoodData, isAdding: Boolean) {
        val currentMap = uiState.value.loggedMeals.toMutableMap()
        val currentList = currentMap[mealKey]?.toMutableList() ?: mutableListOf()
        if (isAdding) {
            if (food !in currentList) currentList.add(food)
        } else {
            currentList.remove(food)
        }
        currentMap[mealKey] = currentList
        _uiState.update { it.copy(loggedMeals = currentMap) }
        calculateTotals()
    }

    fun toggleExercise(exercise: ExerciseData, isAdding: Boolean) {
        val currentList = uiState.value.loggedExercises.toMutableList()
        if (isAdding) {
            if (exercise !in currentList) currentList.add(exercise)
        } else {
            currentList.remove(exercise)
        }
        _uiState.update { it.copy(loggedExercises = currentList) }
    }
}
