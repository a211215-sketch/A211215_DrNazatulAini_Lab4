package com.example.a211215_drnazatulaini_lab4.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211215_drnazatulaini_lab4.ui.FitnessViewModel
import com.example.a211215_drnazatulaini_lab4.ui.components.FoodItemCardBranded
import com.example.a211215_drnazatulaini_lab4.data.getCalories

@Composable
fun MealsHistoryScreen(vm: FitnessViewModel, onBack: () -> Unit) {
    val uiState by vm.uiState.collectAsState()
    val brandPrimary = Color(0xFF2E7D32)

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).safeDrawingPadding()) {
        Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 2.dp) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Text("❮", fontWeight = FontWeight.Bold, color = brandPrimary, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Meal History",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1B1B1B)
                )
            }
        }
        if (uiState.loggedMeals.values.all { it.isEmpty() }) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🍽️", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("No meals recorded today", color = Color.Gray, fontWeight = FontWeight.Medium)
                Text("Go to Home and tap 'Add' to log your meals!", color = Color.Gray, fontSize = 12.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.loggedMeals.forEach { (category, list) ->
                    if (list.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = category.uppercase(),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = brandPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "${list.sumOf { getCalories(it.details) }} kcal",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Divider(modifier = Modifier.padding(top = 4.dp), thickness = 0.5.dp)
                        }

                        items(list) { food ->
                            FoodItemCardBranded(icon = food.icon, title = food.name, subtitle = food.details, brandPrimary = brandPrimary, onClick = { })
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}