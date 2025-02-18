package project.pdm.chatr.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import project.pdm.chatr.APP_TAG
import project.pdm.chatr.model.Habit

/**
 * Repository for managing habits in Firestore
 * @property db Firestore instance
 * @property habitsCollection Firestore collection for habits
 * @constructor Creates a HabitRepository
 */
class HabitRepository {

    private val db = FirebaseFirestore.getInstance()
    private val habitsCollection = db.collection("habits")

    // Retrieves habits as a Flow
    fun getHabits(): Flow<List<Habit>> = callbackFlow {
        val subscription: ListenerRegistration = habitsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(APP_TAG, "getHabits: Error retrieving data", error)
                close(error)
                return@addSnapshotListener
            }
            val habits = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Habit::class.java)
            } ?: emptyList()
            trySend(habits)
        }
        awaitClose { subscription.remove() }
    }

    // Saves or updates a habit in Firestore
    fun saveHabit(habit: Habit) {
        habitsCollection.document(habit.id.toString())
            .set(habit)
            .addOnSuccessListener {
                Log.d(APP_TAG, "saveHabit: Habit saved successfully")
            }
            .addOnFailureListener { e ->
                Log.e(APP_TAG, "saveHabit: Error saving habit", e)
            }
    }

    // Deletes a habit from Firestore
    fun deleteHabit(habit: Habit) {
        habitsCollection.document(habit.id.toString())
            .delete()
            .addOnSuccessListener {
                Log.d(APP_TAG, "deleteHabit: Habit deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.e(APP_TAG, "deleteHabit: Error deleting habit", e)
            }
    }
}
