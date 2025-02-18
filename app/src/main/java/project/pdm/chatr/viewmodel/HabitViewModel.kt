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
     * Atualiza a contagem diária de um hábito e, se necessário, registra a conclusão diária.
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
     * Incrementa as conclusões semanais para o dia atual quando o hábito é completado.
     */
    private fun recordDailyCompletion(habit: Habit) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            // Converte o dia da semana para índice 0 (segunda) a 6 (domingo)
            val dayIndex = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
            val updatedWeek = habit.weeklyCompletions.toMutableList()
            updatedWeek[dayIndex] = updatedWeek[dayIndex] + 1
            val updatedHabit = habit.copy(weeklyCompletions = updatedWeek)
            _habits.value = _habits.value.map { if (it.id == habit.id) updatedHabit else it }
            repository.saveHabit(updatedHabit)
        }
    }

    /**
     * Rola os dados semanais para um novo dia e reseta a contagem diária.
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