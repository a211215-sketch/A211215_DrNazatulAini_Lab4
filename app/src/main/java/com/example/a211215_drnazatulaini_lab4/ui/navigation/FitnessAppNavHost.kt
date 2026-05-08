package com.example.a211215_drnazatulaini_lab4.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a211215_drnazatulaini_lab4.model.FoodData
import kotlin.collections.emptyList
import com.example.a211215_drnazatulaini_lab4.ui.*
import com.example.a211215_drnazatulaini_lab4.ui.screen.DashboardScreen
import com.example.a211215_drnazatulaini_lab4.ui.screen.LogMealScreenBranded
import com.example.a211215_drnazatulaini_lab4.ui.screen.LogExerciseScreenBranded
import com.example.a211215_drnazatulaini_lab4.ui.screen.SettingsScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.a211215_drnazatulaini_lab4.ui.screens.MealsHistoryScreen


@Composable
fun FitnessAppNavHost(
    navController: NavHostController,
    vm: FitnessViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "Dashboard"
    ) {

        composable("Dashboard") {
            DashboardScreen(
                loggedMeals = vm.loggedMeals,
                loggedExercises = vm.loggedExercises,
                onLogMeal = { mealName ->
                    vm.selectedMeal = mealName
                    navController.navigate("LogMeal")
                },
                onLogExercise = {
                    navController.navigate("LogExercise")
                },
                onNavigateSettings = {
                    navController.navigate("Settings")
                },
                onNavigateMeals = {
                    navController.navigate("MealsHistory") },
            )
        }

        composable("LogMeal") {
            val mealKey = vm.uiState.value.selectedMeal ?: ""
            LogMealScreenBranded(
                mealName = mealKey,
                addedFoods = vm.uiState.value.loggedMeals[mealKey] ?: emptyList<FoodData>(),
                onBack = { navController.popBackStack() },
                onFoodToggled = { food, isAdding ->
                    vm.toggleFood(mealKey, food, isAdding)
                }
            )
        }

        composable("LogExercise") {
            val uiState by vm.uiState.collectAsState()

            LogExerciseScreenBranded(
                addedExercises = uiState.loggedExercises,
                onBack = { navController.popBackStack() },
                onExerciseToggled = { exercise, isAdding ->
                    vm.toggleExercise(exercise, isAdding)
                }
            )
        }

        composable("MealsHistory") {
            MealsHistoryScreen(
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable("Settings") {
            SettingsScreen(
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }
    }
}