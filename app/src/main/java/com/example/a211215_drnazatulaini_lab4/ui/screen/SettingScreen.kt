package com.example.a211215_drnazatulaini_lab4.ui.screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a211215_drnazatulaini_lab4.ui.FitnessViewModel
import com.example.a211215_drnazatulaini_lab4.ui.components.SettingsGroupLabel
import com.example.a211215_drnazatulaini_lab4.ui.components.SettingsItem
import com.example.a211215_drnazatulaini_lab4.ui.components.SummaryItem

@Composable
fun SettingsScreen(vm: FitnessViewModel, onBack: () -> Unit) {
    val uiState by vm.uiState.collectAsState()

    var currentWeightInput by remember { mutableStateOf(uiState.currentWeight) }
    var targetWeightInput by remember { mutableStateOf(uiState.targetWeight) }

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
                SummaryItem("${uiState.currentWeight} kg", "Current Weight")
                SummaryItem("${uiState.targetWeight} kg", "Goal Weight")
                SummaryItem("23.6", "Current BMI")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                vm.updateWeight(currentWeightInput, targetWeightInput)
                onBack()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00))
        ) {
            Text("💾 Save & Sync Data", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))
        SettingsGroupLabel("ACCOUNT")
        SettingsItem("Email", "ketty@gmail.com")
        SettingsItem("Sign-up Method", "google")

        Spacer(modifier = Modifier.height(24.dp))
        SettingsGroupLabel("PROFILE")

        OutlinedTextField(
            value = currentWeightInput,
            onValueChange = { currentWeightInput = it },
            label = { Text("Current Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = targetWeightInput,
            onValueChange = { targetWeightInput = it },
            label = { Text("Goal Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))
        SettingsItem("Birth Year", "2005", hasArrow = true)
        SettingsItem("Gender", "Female", hasArrow = true)
        SettingsItem("Height", "153 cm", hasArrow = true)

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { }, modifier = Modifier.fillMaxWidth().height(55.dp).border(1.dp, Color.Black, RoundedCornerShape(30.dp)), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), shape = RoundedCornerShape(30.dp)) {
            Text("Log out", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}