package project.pdm.chatr.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.pdm.chatr.data.HabitRepository
import project.pdm.chatr.model.Habit
import java.util.Calendar

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HabitRepository(application)
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits = _habits.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getHabits().collect { _habits.value = it }
        }
    }

    fun addHabit(habit: Habit) {
        _habits.value += habit
        saveHabits()
    }

    /**
     * Update the daily completion count for a habit.
     * If the daily target is reached, update the weekly completions.
     */
    fun updateHabitCompletion(habit: Habit, newCompleted: Int) {
        _habits.value = _habits.value.map {
            if (it.id == habit.id) it.copy(completedToday = newCompleted) else it
        }
        saveHabits()
        val updatedHabit = _habits.value.find { it.id == habit.id }
        if (updatedHabit != null && updatedHabit.completedToday == updatedHabit.targetPerDay) {
            recordDailyCompletion(updatedHabit)
        }
    }

    fun deleteHabit(habit: Habit) {
        _habits.value = _habits.value.filter { it.id != habit.id }
        saveHabits()
    }

    private fun saveHabits() {
        viewModelScope.launch { repository.saveHabits(_habits.value) }
    }

    /**
     * Increments the weekly completions for the current day.
     * Uses java.util.Calendar to get the current day of the week,
     * converting it to a 0-based index (Monday=0, ..., Sunday=6).
     */
    private fun recordDailyCompletion(habit: Habit) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            // Calendar.DAY_OF_WEEK: Sunday=1, Monday=2, ..., Saturday=7.
            val dayIndex = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
            val updatedWeek = habit.weeklyCompletions.toMutableList()
            updatedWeek[dayIndex] = updatedWeek[dayIndex] + 1

            // Create a new Habit with updated weekly completions.
            val updatedHabit = habit.copy(weeklyCompletions = updatedWeek)
            // Replace the old habit with the updated one in the list.
            _habits.value = _habits.value.map {
                if (it.id == habit.id) updatedHabit else it
            }
            repository.saveHabits(_habits.value)
        }
    }

    /**
     * Shifts the weekly data for a new day.
     * This should be called once per day to roll the weekly data.
     */
    fun shiftWeeklyDataForNewDay() {
        _habits.value = _habits.value.map { habit ->
            val newWeek = habit.weeklyCompletions.drop(1) + 0
            habit.copy(weeklyCompletions = newWeek, completedToday = 0)
        }
        saveHabits()
    }
}
