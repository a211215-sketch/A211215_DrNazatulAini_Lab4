package com.example.a211215_drnazatulaini_lab4.data

import com.example.a211215_drnazatulaini_lab4.model.*

fun getCalories(details: String): Int {
    return try {
        val regex = "(\\d+)\\s*kcal".toRegex()
        val matchResult = regex.find(details)
        matchResult?.groupValues?.get(1)?.toInt() ?: 0
    } catch (e: Exception) { 0 }
}

object DataSource {

    val foods = listOf(
        FoodData("Banana","1 medium • 105 kcal","🍌",protein=1,fiber=3,sodium=1,sugar=14),
        FoodData("Oatmeal","1 cup • 158 kcal","🥣",protein=6,fiber=4,sodium=2,sugar=1),
        FoodData("Chicken Breast","100g • 165 kcal","🍗",protein=31,sodium=74),
        FoodData("Rice","1 bowl • 200 kcal","🍚",protein=4),
        FoodData("Apple","1 fruit • 95 kcal","🍎",fiber=4,sugar=19),
        FoodData("Egg","1 large • 78 kcal","🥚",protein=6,sodium=62),
        FoodData("Bread","1 slice • 79 kcal","🍞",protein=3,sodium=147),
        FoodData("Milk","1 glass • 122 kcal","🥛",protein=8,sodium=100,sugar=12)
    )

    val exercises = listOf(
        ExerciseData("Aerobics", 210, "💃"),
        ExerciseData("Bicycling", 250, "🚴"),
        ExerciseData("Cycling", 310, "🚲"),
        ExerciseData("Hiking", 320, "🥾"),
        ExerciseData("Jogging", 280, "🏃"),
        ExerciseData("Push Ups", 100, "💪"),
        ExerciseData("Running", 450, "🏃‍♂️"),
        ExerciseData("Swimming", 350, "🏊"),
        ExerciseData("Walking", 150, "🚶"),
        ExerciseData("Yoga", 120, "🧘"),
        ExerciseData("Plank", 50, "🧘‍♂️"),
        ExerciseData("Jump Rope", 400, "🪢"),
        ExerciseData("Badminton", 300, "🏸")
    )
}