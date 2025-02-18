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

class HabitRepositoryTest {

    private lateinit var context: Context
    private lateinit var repository: HabitRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = HabitRepository()
        // Optionally, clear the Firestore "habits" collection before each test.
    }

    @After
    fun tearDown() {
        // Optionally, clear the Firestore "habits" collection after each test.
    }

    @Test
    fun testSaveAndLoadHabits() = runBlocking {
        val habit = Habit(
            id = 1,
            name = "Test Habit",
            description = "Save test",
            targetPerDay = 3,
            completedToday = 1
        )
        repository.saveHabit(habit)
        val loadedHabits = repository.getHabits().first()

        println("Expected: 1 habit with name 'Test Habit' and completedToday = 1")
        println("Obtained: ${loadedHabits.size} habit(s): $loadedHabits")

        assertEquals(1, loadedHabits.size)
        assertEquals("Test Habit", loadedHabits[0].name)
        assertEquals(1, loadedHabits[0].completedToday)
    }

    @Test
    fun testSaveAndLoadMultipleHabits() = runBlocking {
        val habit1 = Habit(id = 1, name = "Habit A", description = "Test A", targetPerDay = 3, completedToday = 1)
        val habit2 = Habit(id = 2, name = "Habit B", description = "Test B", targetPerDay = 5, completedToday = 2)
        val habit3 = Habit(id = 3, name = "Habit C", description = "Test C", targetPerDay = 4, completedToday = 4)

        listOf(habit1, habit2, habit3).forEach { repository.saveHabit(it) }
        val loadedHabits = repository.getHabits().first()

        println("Expected: 3 habits with names 'Habit A', 'Habit B', 'Habit C'")
        println("Obtained: ${loadedHabits.size} habit(s): $loadedHabits")

        // Sorting by id since order is not guaranteed.
        val sortedHabits = loadedHabits.sortedBy { it.id }
        assertEquals(3, sortedHabits.size)
        assertEquals("Habit A", sortedHabits[0].name)
        assertEquals("Habit B", sortedHabits[1].name)
        assertEquals("Habit C", sortedHabits[2].name)
    }

    @Test
    fun testSaveEmptyHabitsList() = runBlocking {
        // Assuming no habits are saved
        val loadedHabits = repository.getHabits().first()
        println("Expected: Empty habits list")
        println("Obtained: ${loadedHabits.size} habit(s): $loadedHabits")
        assertTrue(loadedHabits.isEmpty())
    }
}
