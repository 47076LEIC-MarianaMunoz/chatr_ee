package project.pdm.chatr.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import project.pdm.chatr.model.Habit
import java.io.File

private const val APP_TAG = "CHaTrApp"

class HabitRepository(private val context: Context) {

    private val filename = "habits.txt"
    private val habitsFlow = MutableStateFlow<List<Habit>>(emptyList())

    init {
        Log.d(APP_TAG, "HabitRepository init: loading habits from $filename")
        loadHabits()
    }

    fun getHabits(): Flow<List<Habit>> = habitsFlow

    suspend fun saveHabits(habits: List<Habit>) {
        Log.d(APP_TAG, "saveHabits: Saving ${habits.size} habits to file $filename")

        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, filename)
            file.writeText(Json.encodeToString(habits))
        }

        habitsFlow.value = habits
        Log.d(APP_TAG, "saveHabits: Habits saved successfully")
    }

    private fun loadHabits() {
        Log.d(APP_TAG, "loadHabits: Checking if file $filename exists in ${context.filesDir}")
        val file = File(context.filesDir, filename)

        if (file.exists()) {
            val content = file.readText()
            habitsFlow.value = Json.decodeFromString(content)
            Log.d(APP_TAG, "loadHabits: Loaded ${habitsFlow.value.size} habits from file.")
        } else {
            Log.d(APP_TAG, "loadHabits: File does not exist, starting with empty list.")
        }
    }
}
