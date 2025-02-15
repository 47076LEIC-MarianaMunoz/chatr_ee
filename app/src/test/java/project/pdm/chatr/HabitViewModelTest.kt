package project.pdm.chatr

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import project.pdm.chatr.model.Habit
import project.pdm.chatr.viewmodel.HabitViewModel
import java.io.File

@RunWith(RobolectricTestRunner::class)

class HabitViewModelTest {

    private lateinit var application: Application
    private lateinit var viewModel: HabitViewModel
    private val filename = "habits.txt"

    @Before
    fun setup() {
        // Get the test context
        application = ApplicationProvider.getApplicationContext<Context>() as Application
        // Delete the habits file if it exists
        val file = File(application.filesDir, filename)
        if (file.exists()) file.delete()
        viewModel = HabitViewModel(application)
    }

    @After
    fun tearDown() {
        // Clean up the habits file
        val file = File(application.filesDir, filename)
        if (file.exists()) file.delete()
    }

    @Test
    fun testAddHabit() = runBlocking {
        // Given a new habit
        val habit = Habit(id = 123, name = "Habit 1", description = "Description", targetPerDay = 5)

        // When adding the habit
        viewModel.addHabit(habit)
        delay(100) // Wait for the Flow update

        // Then, the habit should be present in the list
        val habits = viewModel.habits.first()
        println("Expected: Habit with id 123 should be present")
        println("Obtained: $habits")
        assertTrue(habits.any { it.id == habit.id })
    }

    @Test
    fun testUpdateHabitCompletion() = runBlocking {
        // Given a new habit
        val habit = Habit(id = 456, name = "Habit 2", description = "Description", targetPerDay = 5)
        viewModel.addHabit(habit)
        delay(100)

        // When updating the habit's completion count to 3
        viewModel.updateHabitCompletion(habit, 3)
        delay(100)

        // Then, the habit's completedToday should be updated
        val habits = viewModel.habits.first()
        val updatedHabit = habits.find { it.id == habit.id }
        println("Expected: Habit with id 456 should have completedToday = 3")
        println("Obtained: $updatedHabit")
        assertNotNull(updatedHabit)
        assertEquals(3, updatedHabit?.completedToday)
    }

    @Test
    fun testDeleteHabit() = runBlocking {
        // Given a new habit
        val habit = Habit(id = 789, name = "Habit 3", description = "Description", targetPerDay = 5)
        viewModel.addHabit(habit)
        delay(100)

        // When deleting the habit
        viewModel.deleteHabit(habit)
        delay(100)

        // Then, the habit should not be present in the list
        val habits = viewModel.habits.first()
        println("Expected: Habit with id 789 should be deleted")
        println("Obtained: $habits")
        assertFalse(habits.any { it.id == habit.id })
    }

    @Test
    fun testMultipleHabits() = runBlocking {
        // Given multiple habits
        val habit1 = Habit(id = 101, name = "Habit A", description = "Desc A", targetPerDay = 3)
        val habit2 = Habit(id = 102, name = "Habit B", description = "Desc B", targetPerDay = 4)
        viewModel.addHabit(habit1)
        viewModel.addHabit(habit2)
        delay(100)

        // Then, both habits should be present
        val habits = viewModel.habits.first()
        println("Expected: Habit A and Habit B should be present")
        println("Obtained: $habits")
        assertTrue(habits.any { it.id == habit1.id })
        assertTrue(habits.any { it.id == habit2.id })
    }

    @Test
    fun testUpdateThenDeleteHabit() = runBlocking {
        // Given a habit
        val habit = Habit(id = 202, name = "Habit UpdateDelete", description = "Update and Delete", targetPerDay = 2)
        viewModel.addHabit(habit)
        delay(100)

        // When updating the habit's progress
        viewModel.updateHabitCompletion(habit, 2)
        delay(100)
        var habits = viewModel.habits.first()
        println("After update, expected completedToday = 2 for habit id 202")
        println("Obtained: $habits")

        // Then, delete the habit
        viewModel.deleteHabit(habit)
        delay(100)
        habits = viewModel.habits.first()
        println("After deletion, expected habit with id 202 to be absent")
        println("Obtained: $habits")
        assertFalse(habits.any { it.id == habit.id })
    }
}
