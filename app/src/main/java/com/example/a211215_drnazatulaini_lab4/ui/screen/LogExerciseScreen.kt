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
import com.example.a211215_drnazatulaini_lab4.model.ExerciseData

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