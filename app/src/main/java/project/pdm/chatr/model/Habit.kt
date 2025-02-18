package project.pdm.chatr.model

import kotlinx.serialization.Serializable

@Serializable
data class Habit(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val targetPerDay: Int = 0,
    var completedToday: Int = 0,
    val weeklyCompletions: List<Int> = listOf(0, 0, 0, 0, 0, 0, 0)
)
