package com.example.a211215_drnazatulaini_lab4.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211215_drnazatulaini_lab4.data.getCalories
import com.example.a211215_drnazatulaini_lab4.model.ExerciseData
import com.example.a211215_drnazatulaini_lab4.model.FoodData
import com.example.a211215_drnazatulaini_lab4.ui.FitnessViewModel
import com.example.a211215_drnazatulaini_lab4.ui.components.BottomNavItemBranded
import com.example.a211215_drnazatulaini_lab4.ui.components.FoodItemCardBranded
import com.example.a211215_drnazatulaini_lab4.ui.components.NutrientBarBranded


@Composable
fun DashboardScreen(
    loggedMeals: Map<String, List<FoodData>>,
    loggedExercises: List<ExerciseData>,
    onLogMeal: (String) -> Unit,
    onLogExercise: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateMeals: () -> Unit
) {
    val brandPrimary = MaterialTheme.colorScheme.primary
    val brandSurface = MaterialTheme.colorScheme.surface
    val textDark = MaterialTheme.colorScheme.onBackground
    val textMuted = MaterialTheme.colorScheme.secondary

    val allLoggedFoods = loggedMeals.values.flatten()
    val totalIntake = allLoggedFoods.sumOf { getCalories(it.details) }
    val totalBurned = loggedExercises.sumOf { it.caloriesBurned }
    val remainingCalories = 2000 - totalIntake + totalBurned

    var isExpanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    val totalFat = allLoggedFoods.sumOf { it.fat }
    val totalCarbs = allLoggedFoods.sumOf { it.carbs }
    val totalProtein = allLoggedFoods.sumOf { it.protein }
    val totalFiber = allLoggedFoods.sumOf { it.fiber }
    val totalSodium = allLoggedFoods.sumOf { it.sodium }
    val totalSugar = allLoggedFoods.sumOf { it.sugar }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).safeDrawingPadding()) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 90.dp).padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "❮", fontSize = 16.sp, color = textMuted)
                Text(text = "TODAY", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = textDark)
                Text(text = "❯", fontSize = 16.sp, color = textMuted)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth().animateContentSize().clickable { isExpanded = !isExpanded },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = brandSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = if (isExpanded) "Show Less " else "Show Details ", fontSize = 12.sp, color = brandPrimary, fontWeight = FontWeight.Bold)
                            Text(text = "▼", fontSize = 10.sp, color = brandPrimary, modifier = Modifier.graphicsLayer { rotationZ = rotationState })
                        }
                        Text(text = "Edit Budget ⚙️", fontSize = 12.sp, color = textMuted)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("INTAKE", fontSize = 11.sp, color = textMuted, fontWeight = FontWeight.Bold)
                            Text("$totalIntake", style = MaterialTheme.typography.displayMedium)
                        }
                        Box(modifier = Modifier.size(110.dp).border(8.dp, brandPrimary, CircleShape), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("LEFT", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("$remainingCalories", style = MaterialTheme.typography.displayMedium, fontSize = 28.sp)
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("BURNED", fontSize = 11.sp, color = textMuted, fontWeight = FontWeight.Bold)
                            Text("$totalBurned", style = MaterialTheme.typography.displayMedium)
                        }
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column {
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                NutrientBarBranded(Modifier.weight(1f), "Fat", "${totalFat}/56g", (totalFat/56f), Color(0xFFF59E0B))
                                NutrientBarBranded(Modifier.weight(1f), "Net Carbs", "${totalCarbs}/78g", (totalCarbs/78f), Color(0xFFEF4444))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                NutrientBarBranded(Modifier.weight(1f), "Protein", "${totalProtein}/156g", (totalProtein/156f), Color(0xFF3B82F6))
                                NutrientBarBranded(Modifier.weight(1f), "Fiber", "${totalFiber}/56g", (totalFiber/56f), Color(0xFF10B981))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                NutrientBarBranded(Modifier.weight(1f), "Sodium", "${totalSodium}/2300mg", (totalSodium/2300f), Color(0xFF6B7280))
                                NutrientBarBranded(Modifier.weight(1f), "Sugar", "${totalSugar}/50g", (totalSugar/50f), Color(0xFFEC4899))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Nutrition Log", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            listOf("Breakfast", "Lunch", "Dinner", "Snack").forEach { meal ->
                val list = loggedMeals[meal] ?: emptyList()
                FoodItemCardBranded(
                    icon = when(meal) { "Breakfast" -> "🥐" "Lunch" -> "🥑" "Dinner" -> "🥩" else -> "🍎" },
                    title = meal,
                    subtitle = if (list.isEmpty()) "Tap to add..." else list.joinToString { it.name },
                    brandPrimary = brandPrimary
                ) { onLogMeal(meal) }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Exercise Log", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            FoodItemCardBranded(
                icon = "🏋️",
                title = "Exercise Burn",
                subtitle = if(loggedExercises.isEmpty()) "Tap to add..." else loggedExercises.joinToString { it.name },
                brandPrimary = brandPrimary,
                onClick = { onLogExercise() }
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().height(70.dp).align(Alignment.BottomCenter)) {
            Surface(modifier = Modifier.fillMaxSize(), color = brandSurface, shadowElevation = 8.dp, border = BorderStroke(0.5.dp, textMuted.copy(alpha = 0.2f))) {
                Row(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                    BottomNavItemBranded(icon = "⚡", label = "Energy", isActive = true, activeColor = brandPrimary, onClick = { })
                    BottomNavItemBranded(icon = "🍽️", label = "Meals", isActive = false, activeColor = brandPrimary,onClick = { onNavigateMeals() })
                    BottomNavItemBranded(icon = "⚙️", label = "Settings", isActive = false, activeColor = brandPrimary, onClick = { onNavigateSettings() })
                }
            }
        }
    }
}
