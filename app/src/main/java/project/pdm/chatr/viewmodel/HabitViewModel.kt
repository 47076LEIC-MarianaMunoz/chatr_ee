package project.pdm.chatr.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.pdm.chatr.data.HabitRepository
import project.pdm.chatr.model.Habit

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "CHaTr"
    }
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

    fun updateHabitCompletion(habit: Habit, completed: Int) {
        _habits.value = _habits.value.map {
            if (it.id == habit.id) it.copy(completedToday = completed) else it
        }
        saveHabits()
    }

    fun deleteHabit(habit: Habit) {
        _habits.value = _habits.value.filter { it.id != habit.id }
        saveHabits()
    }

    private fun saveHabits() {
        viewModelScope.launch { repository.saveHabits(_habits.value) }
    }
}
