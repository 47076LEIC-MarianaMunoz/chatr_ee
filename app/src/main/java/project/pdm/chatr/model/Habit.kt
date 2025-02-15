package project.pdm.chatr.model

import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val id: Int,
    val name: String,
    val description: String,
    val targetPerDay: Int,
    var completedToday: Int = 0,
    val weeklyCompletions: List<Int> = listOf(0, 0, 0, 0, 0, 0, 0)
)
