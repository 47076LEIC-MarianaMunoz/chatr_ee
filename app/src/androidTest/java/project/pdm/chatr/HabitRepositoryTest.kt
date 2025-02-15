package project.pdm.chatr

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import project.pdm.chatr.data.HabitRepository
import project.pdm.chatr.model.Habit
import java.io.File

class HabitRepositoryTest {

    private lateinit var context: Context
    private lateinit var repository: HabitRepository
    private val filename = "habits.txt"

    @Before
    fun setup() {
        // Get the test context and delete the habits file if it exists to start clean
        context = ApplicationProvider.getApplicationContext<Context>()
        val file = File(context.filesDir, filename)
        if (file.exists()) file.delete()
        repository = HabitRepository(context)
    }

    @After
    fun tearDown() {
        // Clean up the habits file after tests
        val file = File(context.filesDir, filename)
        if (file.exists()) file.delete()
    }

    @Test
    fun testSaveAndLoadHabits() = runBlocking {
        // Given a list with one habit
        val habit = Habit(
            id = 1,
            name = "Test Habit",
            description = "Save test",
            targetPerDay = 3,
            completedToday = 1
        )
        val habitsList = listOf(habit)

        // When saving the habits
        repository.saveHabits(habitsList)

        // Then, loading should return the same list
        val loadedHabits = repository.getHabits().first()
        println("Expected: 1 habit with name 'Test Habit' and completedToday = 1")
        println("Obtained: ${loadedHabits.size} habit(s): $loadedHabits")

        assertEquals(1, loadedHabits.size)
        assertEquals("Test Habit", loadedHabits[0].name)
        assertEquals(1, loadedHabits[0].completedToday)
    }

    @Test
    fun testSaveAndLoadMultipleHabits() = runBlocking {
        // Given a list with multiple habits
        val habit1 = Habit(id = 1, name = "Habit A", description = "Test A", targetPerDay = 3, completedToday = 1)
        val habit2 = Habit(id = 2, name = "Habit B", description = "Test B", targetPerDay = 5, completedToday = 2)
        val habit3 = Habit(id = 3, name = "Habit C", description = "Test C", targetPerDay = 4, completedToday = 4)
        val habitsList = listOf(habit1, habit2, habit3)

        // When saving the habits
        repository.saveHabits(habitsList)

        // Then, loading should return the same list of habits
        val loadedHabits = repository.getHabits().first()
        println("Expected: 3 habits with names 'Habit A', 'Habit B', 'Habit C'")
        println("Obtained: ${loadedHabits.size} habit(s): $loadedHabits")

        assertEquals(3, loadedHabits.size)
        assertEquals("Habit A", loadedHabits[0].name)
        assertEquals("Habit B", loadedHabits[1].name)
        assertEquals("Habit C", loadedHabits[2].name)
    }

    @Test
    fun testSaveEmptyHabitsList() = runBlocking {
        // Given an empty habits list
        val habitsList = emptyList<Habit>()

        // When saving the empty list
        repository.saveHabits(habitsList)

        // Then, loading should return an empty list
        val loadedHabits = repository.getHabits().first()
        println("Expected: Empty habits list")
        println("Obtained: ${loadedHabits.size} habit(s): $loadedHabits")

        assertTrue(loadedHabits.isEmpty())
    }
}
