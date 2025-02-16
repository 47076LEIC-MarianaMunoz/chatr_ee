package project.pdm.chatr.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import project.pdm.chatr.APP_TAG
import project.pdm.chatr.model.Habit

// Extensão para criar o DataStore
private val Context.dataStore by preferencesDataStore(name = "habits_datastore")

/**
 * Repository for managing the list of habits using DataStore.
 *
 * @param context The application context used to access the DataStore.
 *
 * Note: The Habit model should include the weeklyCompletions field.
 *      This field will be automatically serialized and deserialized.
 */
class HabitRepository(private val context: Context) {

    // Key to store/retrieve the habits JSON string.
    private val HABITS_KEY = stringPreferencesKey("habits_key")

    /**
     * Returns a flow of habits.
     */
    fun getHabits(): Flow<List<Habit>> = context.dataStore.data.map { preferences ->
        preferences[HABITS_KEY]?.let { jsonString ->
            try {
                Json.decodeFromString(jsonString)
            } catch (e: Exception) {
                Log.e(APP_TAG, "getHabits: Error decoding habits JSON", e)
                emptyList()
            }
        } ?: run {
            Log.d(APP_TAG, "getHabits: No habits found, returning empty list")
            emptyList()
        }
    }

    /**
     * Saves the list of habits to the DataStore.
     */
    suspend fun saveHabits(habits: List<Habit>) {
        Log.d(APP_TAG, "saveHabits: Saving ${habits.size} habits to DataStore")
        context.dataStore.edit { preferences ->
            preferences[HABITS_KEY] = Json.encodeToString(habits)
        }
        Log.d(APP_TAG, "saveHabits: Habits saved successfully")
    }
}
