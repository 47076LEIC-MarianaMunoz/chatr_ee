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
    private val repository = HabitRepository()
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits = _habits.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getHabits().collect { habitList ->
                _habits.value = habitList
            }
        }
    }

    fun addHabit(habit: Habit) {
        _habits.value += habit
        saveHabit(habit)
    }

    /**
     * Updates the daily count of a habit and, if necessary, records the daily completion.
     */
    fun updateHabitCompletion(habit: Habit, newCompleted: Int) {
        _habits.value = _habits.value.map {
            if (it.id == habit.id) it.copy(completedToday = newCompleted) else it
        }
        val updatedHabit = _habits.value.find { it.id == habit.id }
        updatedHabit?.let { saveHabit(it) }
        if (updatedHabit != null && updatedHabit.completedToday == updatedHabit.targetPerDay) {
            recordDailyCompletion(updatedHabit)
        }
    }

    fun deleteHabit(habit: Habit) {
        _habits.value = _habits.value.filter { it.id != habit.id }
        viewModelScope.launch { repository.deleteHabit(habit) }
    }

    private fun saveHabit(habit: Habit) {
        viewModelScope.launch { repository.saveHabit(habit) }
    }

    /**
     * Increments the weekly completion count for the current day when the habit is completed.
     */
    private fun recordDailyCompletion(habit: Habit) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            // Converts the weekday to an index from 0 (Monday) to 6 (Sunday)
            val dayIndex = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
            val updatedWeek = habit.weeklyCompletions.toMutableList()
            updatedWeek[dayIndex] = updatedWeek[dayIndex] + 1
            val updatedHabit = habit.copy(weeklyCompletions = updatedWeek)
            _habits.value = _habits.value.map { if (it.id == habit.id) updatedHabit else it }
            repository.saveHabit(updatedHabit)
        }
    }

    /**
     * Rolls the weekly data to a new day and resets the daily count.
     */
    fun shiftWeeklyDataForNewDay() {
        _habits.value = _habits.value.map { habit ->
            val newWeek = habit.weeklyCompletions.drop(1) + 0
            habit.copy(weeklyCompletions = newWeek, completedToday = 0)
        }
        viewModelScope.launch {
            _habits.value.forEach { repository.saveHabit(it) }
        }
    }
}
