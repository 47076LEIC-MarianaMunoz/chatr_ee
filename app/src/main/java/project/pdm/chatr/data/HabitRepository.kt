package project.pdm.chatr.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import project.pdm.chatr.model.Habit
import java.io.File

class HabitRepository(private val context: Context) {

    private val filename = "habits.txt"
    private val habitsFlow = MutableStateFlow<List<Habit>>(emptyList())

    init {
        loadHabits()
    }

    fun getHabits(): Flow<List<Habit>> = habitsFlow

    suspend fun saveHabits(habits: List<Habit>) {
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, filename)
            file.writeText(Json.encodeToString(habits))
        }
        habitsFlow.value = habits
    }

    private fun loadHabits() {
        val file = File(context.filesDir, filename)
        if (file.exists()) {
            val content = file.readText()
            habitsFlow.value = Json.decodeFromString(content)
        }
    }
}
