package com.example.a211215_drnazatulaini_lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a211215_drnazatulaini_lab4.ui.theme.A211215_DrNazatulAini_Lab4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            A211215_DrNazatulAini_Lab4Theme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    FitnessApp()
                }
            }
        }
    }
}

data class FoodData(
    val name: String, val details: String, val icon: String,
    val fat: Int = 0, val carbs: Int = 0, val protein: Int = 0,
    val fiber: Int = 0, val sodium: Int = 0, val sugar: Int = 0
)

data class ExerciseData(val name: String, val caloriesBurned: Int, val icon: String)

fun getCalories(details: String): Int = try {
    details.substringAfter("•").replace("kcal", "").trim().toInt()
} catch (e: Exception) { 0 }

class FitnessViewModel : ViewModel() {
    var loggedMeals by mutableStateOf(mapOf<String, List<FoodData>>())
    var loggedExercises by mutableStateOf(listOf<ExerciseData>())
    var selectedMeal by mutableStateOf("")

    fun toggleFood(mealName: String, food: FoodData, isAdding: Boolean) {
        val currentList = loggedMeals[mealName] ?: emptyList()
        loggedMeals = if (isAdding) loggedMeals + (mealName to (currentList + food))
        else loggedMeals + (mealName to currentList.filter { it.name != food.name })
    }

    fun toggleExercise(exercise: ExerciseData, isAdding: Boolean) {
        loggedExercises = if (isAdding) loggedExercises + exercise
        else loggedExercises.filter { it.name != exercise.name }
    }
}

@Composable
fun FitnessApp() {
    val navController = rememberNavController()
    val vm: FitnessViewModel = viewModel()

    NavHost(navController = navController, startDestination = "Dashboard") {
        composable("Dashboard") {
            FitnessDashboardBranded(
                loggedMeals = vm.loggedMeals,
                loggedExercises = vm.loggedExercises,
                onLogMeal = { mealName ->
                    vm.selectedMeal = mealName
                    navController.navigate("LogMeal")
                },
                onLogExercise = { navController.navigate("LogExercise") },
                onNavigateSettings = { navController.navigate("Settings") }
            )
        }
        composable("LogMeal") {
            LogMealScreenBranded(
                mealName = vm.selectedMeal,
                addedFoods = vm.loggedMeals[vm.selectedMeal] ?: emptyList(),
                onBack = { navController.popBackStack() },
                onFoodToggled = { food, isAdding -> vm.toggleFood(vm.selectedMeal, food, isAdding) }
            )
        }
        composable("LogExercise") {
            LogExerciseScreenBranded(
                addedExercises = vm.loggedExercises,
                onBack = { navController.popBackStack() },
                onExerciseToggled = { ex, isAdding -> vm.toggleExercise(ex, isAdding) }
            )
        }
        composable("Settings") {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun FitnessDashboardBranded(
    loggedMeals: Map<String, List<FoodData>>,
    loggedExercises: List<ExerciseData>,
    onLogMeal: (String) -> Unit,
    onLogExercise: () -> Unit,
    onNavigateSettings: () -> Unit
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

        // Bottom Nav Bar
        Box(modifier = Modifier.fillMaxWidth().height(80.dp).align(Alignment.BottomCenter)) {
            Box(modifier = Modifier.fillMaxWidth().height(60.dp).background(brandSurface).border(0.5.dp, textMuted.copy(alpha = 0.2f)).align(Alignment.BottomCenter)) {
                Row(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    BottomNavItemBranded(icon = "⚡", label = "Energy", isActive = true, activeColor = brandPrimary, onClick = { })
                    BottomNavItemBranded(icon = "📊", label = "Stats", isActive = false, activeColor = brandPrimary, onClick = { })
                    Spacer(modifier = Modifier.width(60.dp))
                    BottomNavItemBranded(icon = "🍽️", label = "Meals", isActive = false, activeColor = brandPrimary, onClick = { })
                    BottomNavItemBranded(icon = "⚙️", label = "Settings", isActive = false, activeColor = brandPrimary, onClick = { onNavigateSettings() })
                }
            }
            Box(modifier = Modifier.size(64.dp).background(brandPrimary, RoundedCornerShape(16.dp)).border(4.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp)).align(Alignment.TopCenter), contentAlignment = Alignment.Center) {
                Text(text = "＋", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogExerciseScreenBranded(addedExercises: List<ExerciseData>, onBack: () -> Unit, onExerciseToggled: (ExerciseData, Boolean) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val allExercises = listOf(
        ExerciseData("Aerobics", 210, "💃"), ExerciseData("Bicycling", 250, "🚴"),
        ExerciseData("Cycling", 310, "🚲"), ExerciseData("Hiking", 320, "🥾"),
        ExerciseData("Jogging", 280, "🏃"), ExerciseData("Push Ups", 100, "💪"),
        ExerciseData("Running", 450, "🏃‍♂️"), ExerciseData("Swimming", 350, "🏊"),
        ExerciseData("Walking", 150, "🚶"), ExerciseData("Yoga", 120, "🧘"),
        ExerciseData("Plank", 50, "🧘‍♂️"), ExerciseData("Jump Rope", 400, "🪢"),
        ExerciseData("Badminton", 300, "🏸"), ExerciseData("Football", 500, "⚽")
    ).sortedBy { it.name }

    val filteredExercises = allExercises.filter { it.name.contains(searchQuery, ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).safeDrawingPadding().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("❮", modifier = Modifier.clickable { onBack() }, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Log Exercise", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("✔", modifier = Modifier.clickable { onBack() }, color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = searchQuery, onValueChange = { searchQuery = it },
            placeholder = { Text("Search exercise...") },
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent)
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(filteredExercises) { exercise ->
                val isAdded = addedExercises.any { it.name == exercise.name }
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).clickable { onExerciseToggled(exercise, !isAdded) }.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(exercise.icon, fontSize = 24.sp); Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(exercise.name, fontWeight = FontWeight.Bold)
                                Text("${exercise.caloriesBurned} kcal", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        Text(if (isAdded) "✔" else "＋", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogMealScreenBranded(mealName: String, addedFoods: List<FoodData>, onBack: () -> Unit, onFoodToggled: (FoodData, Boolean) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val allFoods = listOf(
        FoodData("Banana", "1 medium • 105 kcal", "🍌", protein = 1, fiber = 3, sodium = 1, sugar = 14),
        FoodData("Oatmeal", "1 cup • 158 kcal", "🥣", protein = 6, fiber = 4, sodium = 2, sugar = 1),
        FoodData("Chicken Breast", "100g • 165 kcal", "🍗", protein = 31, fiber = 0, sodium = 74, sugar = 0),
        FoodData("Rice", "1 bowl • 200 kcal", "🍚", protein = 4, fiber = 1, sodium = 1, sugar = 0),
        FoodData("Apple", "1 fruit • 95 kcal", "🍎", protein = 1, fiber = 4, sodium = 1, sugar = 19),
        FoodData("Egg", "1 large • 78 kcal", "🥚", protein = 6, fiber = 0, sodium = 62, sugar = 0),
        FoodData("Bread", "1 slice • 79 kcal", "🍞", protein = 3, fiber = 1, sodium = 147, sugar = 1),
        FoodData("Milk", "1 glass • 122 kcal", "🥛", protein = 8, fiber = 0, sodium = 100, sugar = 12)
    )
    val filteredFoods = allFoods.filter { it.name.contains(searchQuery, ignoreCase = true) }
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).safeDrawingPadding().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("❮", modifier = Modifier.clickable { onBack() }, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Log $mealName", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("✔", modifier = Modifier.clickable { onBack() }, color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = searchQuery, onValueChange = { searchQuery = it },
            placeholder = { Text("Search for item...") },
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent)
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            items(filteredFoods) { food ->
                val isAdded = addedFoods.any { it.name == food.name }
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).clickable { onFoodToggled(food, !isAdded) }.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(food.icon, fontSize = 24.sp); Spacer(modifier = Modifier.width(16.dp))
                            Column { Text(food.name, fontWeight = FontWeight.Bold); Text(food.details, fontSize = 12.sp) }
                        }
                        Text(if (isAdded) "✔" else "＋", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState()).safeDrawingPadding().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("❮", modifier = Modifier.clickable { onBack() }.padding(8.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text("Profile", style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(60.dp).background(Color(0xFFFFE0B2), CircleShape), contentAlignment = Alignment.Center) {
                Text("🔥", fontSize = 30.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Ketty", style = MaterialTheme.typography.titleLarge, fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1B2A)), shape = RoundedCornerShape(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.SpaceAround) {
                SummaryItem("55.2 kg", "Current Weight")
                SummaryItem("45.0 kg", "Goal Weight")
                SummaryItem("23.6", "Current BMI")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(25.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF57C00))) {
            Text("💬 Help & Feedback", color = Color(0xFFF57C00))
        }
        Spacer(modifier = Modifier.height(32.dp))
        SettingsGroupLabel("ACCOUNT")
        SettingsItem("Email", "ketty@gmail.com")
        SettingsItem("Sign-up Method", "google")
        Spacer(modifier = Modifier.height(24.dp))
        SettingsGroupLabel("PROFILE")
        SettingsItem("Name", "Ketty", hasArrow = true)
        SettingsItem("Birth Year", "2005", hasArrow = true)
        SettingsItem("Gender", "Female", hasArrow = true)
        SettingsItem("Height", "153 cm", hasArrow = true)
        SettingsItem("Weight", "55.2 kg", hasArrow = true)
        SettingsItem("Goal Weight", "45.0 kg", hasArrow = true)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { }, modifier = Modifier.fillMaxWidth().height(55.dp).border(1.dp, Color.Black, RoundedCornerShape(30.dp)), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), shape = RoundedCornerShape(30.dp)) {
            Text("Log out", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

@Composable
fun SummaryItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = Color.LightGray, fontSize = 11.sp)
    }
}
@Composable
fun SettingsGroupLabel(text: String) {
    Text(text = text, fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
}
@Composable
fun SettingsItem(label: String, value: String, hasArrow: Boolean = false) {
    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(label, fontWeight = FontWeight.Medium, fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (value.isNotEmpty()) Text(value, color = Color.Gray, fontSize = 16.sp)
                if (hasArrow) { Spacer(modifier = Modifier.width(8.dp)); Text("❯", color = Color.LightGray, fontSize = 14.sp) }
            }
        }
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)
    }
}
@Composable
fun FoodItemCardBranded(icon: String, title: String, subtitle: String, brandPrimary: Color, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp)).border(1.dp, Color(0xFFD9E2EC), RoundedCornerShape(12.dp)).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Text(icon, fontSize = 28.sp); Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(subtitle, fontSize = 12.sp, color = if (subtitle == "Tap to add...") Color.Gray else brandPrimary, maxLines = 1)
                }
            }
            Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { onClick() }.background(brandPrimary.copy(alpha = 0.1f)).padding(horizontal = 14.dp, vertical = 8.dp)) {
                Text(text = if (subtitle == "Tap to add...") "ADD +" else "EDIT", color = brandPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            }
        }
    }
}
@Composable
fun NutrientBarBranded(modifier: Modifier, label: String, value: String, fraction: Float, color: Color) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = value, fontSize = 11.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Color(0xFFE2E8F0), RoundedCornerShape(2.dp))) {
            Box(modifier = Modifier.fillMaxWidth(fraction.coerceIn(0f, 1f)).height(4.dp).background(color, RoundedCornerShape(2.dp)))
        }
    }
}
@Composable
fun BottomNavItemBranded(icon: String, label: String, isActive: Boolean, activeColor: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }.padding(4.dp)) {
        Text(text = icon, fontSize = 20.sp)
        Text(text = label, fontSize = 10.sp, color = if (isActive) activeColor else Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun FitnessAppPreview() {
    A211215_DrNazatulAini_Lab4Theme { FitnessApp() }
}