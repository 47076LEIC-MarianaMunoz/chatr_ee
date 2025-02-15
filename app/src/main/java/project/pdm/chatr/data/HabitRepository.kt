package project.pdm.chatr.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import project.pdm.chatr.APP_TAG
import project.pdm.chatr.model.Habit
import java.io.File

/**
 * Repository for managing the list of habits.
 *
 * @param context The application context used to access the file system.
 *
 * Note: The Habit model should include the weeklyCompletions field.
 *      This field will be automatically serialized and deserialized.
 */
class HabitRepository(private val context: Context) {

    private val filename = "habits.txt"
    private val habitsFlow = MutableStateFlow<List<Habit>>(emptyList())

    init {
        // Load habits from file when the repository is initialized.
        Log.d(APP_TAG, "HabitRepository init: loading habits from $filename")
        loadHabits()
    }

    // Returns a flow of habits.
    fun getHabits(): Flow<List<Habit>> = habitsFlow

    // Saves the list of habits to a file and updates the flow.
    suspend fun saveHabits(habits: List<Habit>) {
        Log.d(APP_TAG, "saveHabits: Saving ${habits.size} habits to file $filename")
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, filename)
            file.writeText(Json.encodeToString(habits))
        }
        habitsFlow.value = habits
        Log.d(APP_TAG, "saveHabits: Habits saved successfully")
    }

    // Loads habits from the file if it exists.
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
