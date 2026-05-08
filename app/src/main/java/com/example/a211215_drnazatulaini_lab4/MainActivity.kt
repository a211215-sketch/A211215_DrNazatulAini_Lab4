package com.example.a211215_drnazatulaini_lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.a211215_drnazatulaini_lab4.ui.FitnessViewModel
import com.example.a211215_drnazatulaini_lab4.ui.navigation.FitnessAppNavHost
import com.example.a211215_drnazatulaini_lab4.ui.theme.A211215_DrNazatulAini_Lab4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            A211215_DrNazatulAini_Lab4Theme {

                val navController = rememberNavController()
                val vm: FitnessViewModel = viewModel()

                FitnessAppNavHost(
                    navController = navController,
                    vm = vm
                )
            }
        }
    }
}