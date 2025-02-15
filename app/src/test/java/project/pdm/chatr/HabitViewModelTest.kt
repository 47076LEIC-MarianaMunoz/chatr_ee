package project.pdm.chatr

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import project.pdm.chatr.model.Habit
import project.pdm.chatr.viewmodel.HabitViewModel
import java.io.File
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class HabitViewModelTest {

    // Test dispatcher for Dispatchers.Main
    private val testDispatcher = StandardTestDispatcher()

    // Unique temporary directory for each test run
    private lateinit var tempDir: File
    private val filename = "habits.txt"
    private lateinit var fakeApplication: Application
    private lateinit var viewModel: HabitViewModel

    @Before
    fun setup() {
        // Set Dispatchers.Main to the test dispatcher
        Dispatchers.setMain(testDispatcher)
        // Create a unique temporary directory for the test
        tempDir = File(System.getProperty("java.io.tmpdir"), "chatr_test_${UUID.randomUUID()}")
        if (!tempDir.exists()) tempDir.mkdirs()
        // Fake Application returns the temporary directory as filesDir
        fakeApplication = object : Application() {
            override fun getFilesDir(): File = tempDir
        }
        // Clean up any existing file
        val file = File(tempDir, filename)
        if (file.exists()) file.delete()
        // Initialize the ViewModel
        viewModel = HabitViewModel(fakeApplication)
    }

    @After
    fun tearDown() {
        // Reset Dispatchers.Main and delete the temporary directory
        Dispatchers.resetMain()
        tempDir.deleteRecursively()
    }

    @Test
    fun testAddHabit() = runTest {
        println("\n=== Test: Add Habit ===")
        val habit = Habit(id = 123, name = "Habit 1", description = "Description", targetPerDay = 5)
        println("Step 1: Adding habit:\n  $habit")
        viewModel.addHabit(habit)
        advanceUntilIdle()
        val habits = viewModel.habits.first()
        println("Step 2: Habits list after addition:")
        habits.forEachIndexed { index, h -> println("  [$index] $h") }
        println("Expected: Habit with id ${habit.id} should be present.")
        assertTrue(habits.any { it.id == habit.id })
    }

    @Test
    fun testUpdateHabitCompletion() = runTest {
        println("\n=== Test: Update Habit Completion ===")
        val habit = Habit(id = 456, name = "Habit 2", description = "Description", targetPerDay = 5)
        println("Step 1: Adding habit:\n  $habit")
        viewModel.addHabit(habit)
        advanceUntilIdle()
        println("Step 2: Updating habit (id=${habit.id}) to have completedToday = 3")
        viewModel.updateHabitCompletion(habit, 3)
        advanceUntilIdle()
        val habits = viewModel.habits.first()
        val updatedHabit = habits.find { it.id == habit.id }
        println("Step 3: Habits list after update:")
        habits.forEachIndexed { index, h -> println("  [$index] $h") }
        println("Expected: Habit with id ${habit.id} should have completedToday = 3.")
        println("Obtained: $updatedHabit")
        assertNotNull(updatedHabit)
        assertEquals(3, updatedHabit?.completedToday)
    }

    @Test
    fun testDeleteHabit() = runTest {
        println("\n=== Test: Delete Habit ===")
        val habit = Habit(id = 789, name = "Habit 3", description = "Description", targetPerDay = 5)
        println("Step 1: Adding habit:\n  $habit")
        viewModel.addHabit(habit)
        advanceUntilIdle()
        var habits = viewModel.habits.first()
        println("Step 2: Habits list before deletion:")
        habits.forEachIndexed { index, h -> println("  [$index] $h") }
        println("Step 3: Deleting habit with id ${habit.id}")
        viewModel.deleteHabit(habit)
        advanceUntilIdle()
        habits = viewModel.habits.first()
        println("Step 4: Habits list after deletion:")
        if (habits.isEmpty()) {
            println("  [List is empty]")
        } else {
            habits.forEachIndexed { index, h -> println("  [$index] $h") }
        }
        println("Expected: Habit with id ${habit.id} should be absent.")
        assertFalse("Habit with id ${habit.id} was not deleted", habits.any { it.id == habit.id })
    }

    @Test
    fun testMultipleHabits() = runTest {
        println("\n=== Test: Multiple Habits ===")
        val habit1 = Habit(id = 101, name = "Habit A", description = "Desc A", targetPerDay = 3)
        val habit2 = Habit(id = 102, name = "Habit B", description = "Desc B", targetPerDay = 4)
        println("Step 1: Adding habit:\n  $habit1")
        println("Step 1: Adding habit:\n  $habit2")
        viewModel.addHabit(habit1)
        viewModel.addHabit(habit2)
        advanceUntilIdle()
        val habits = viewModel.habits.first()
        println("Step 2: Habits list after adding multiple habits:")
        habits.forEachIndexed { index, h -> println("  [$index] $h") }
        println("Expected: Habits with ids ${habit1.id} and ${habit2.id} should be present.")
        assertTrue(habits.any { it.id == habit1.id })
        assertTrue(habits.any { it.id == habit2.id })
    }
}
