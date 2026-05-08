package com.example.a211215_drnazatulaini_lab4.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211215_drnazatulaini_lab4.model.FoodData

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