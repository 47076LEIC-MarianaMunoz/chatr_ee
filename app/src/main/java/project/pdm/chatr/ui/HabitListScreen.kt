package project.pdm.chatr.ui

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import project.pdm.chatr.APP_TAG
import project.pdm.chatr.viewmodel.HabitViewModel
import project.pdm.chatr.model.Habit
import project.pdm.chatr.ui.theme.Amber40
import project.pdm.chatr.ui.theme.Amber80
import project.pdm.chatr.ui.theme.Blue40
import project.pdm.chatr.ui.theme.BlueProgress


/**
 * Displays the Habit List Screen.
 *
 * This screen shows:
 * - A TopAppBar with a back button.
 * - A LazyColumn of habit items.
 * - Two floating action buttons for navigating to the stats screen and the habit form.
 *
 * @param viewModel The HabitViewModel that supplies the list of habits and handles operations.
 * @param navController Navigation controller for screen transitions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(viewModel: HabitViewModel, navController: NavHostController) {
    Log.d(APP_TAG, "HabitListScreen: Displaying Habit List Screen")
    // Collect habits from the ViewModel as state.
    val habits by viewModel.habits.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column {
            // Top AppBar with a back button to navigate back to the entry screen.
            TopAppBar(
                title = {
                    Text("Habits", color = Color.Black, fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        Log.d(APP_TAG, "HabitListScreen: Navigating back to Entry Screen")
                        navController.navigate("entry")
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Entry",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                // LazyColumn displays the list of habits.
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(habits) { habit ->
                        HabitItem(
                            habit = habit,
                            onIncrement = {
                                Log.d(APP_TAG, "HabitListScreen: Incrementing habit '${habit.name}'")
                                viewModel.updateHabitCompletion(habit, habit.completedToday + 1)
                            },
                            onDecrement = {
                                Log.d(APP_TAG, "HabitListScreen: Decrementing habit '${habit.name}'")
                                viewModel.updateHabitCompletion(habit, (habit.completedToday - 1).coerceAtLeast(0))
                            },
                            onDelete = {
                                Log.d(APP_TAG, "HabitListScreen: Deleting habit '${habit.name}'")
                                viewModel.deleteHabit(habit)
                            }
                        )
                    }
                }

                // Column with Floating Action Buttons (FABs) for stats and adding a new habit.
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    // FAB to navigate to the Habit Stats screen.
                    FloatingActionButton(
                        onClick = {
                            Log.d(APP_TAG, "HabitListScreen: Navigating to Habit Stats Screen")
                            navController.navigate("habitStats")
                        },
                        containerColor = Blue40,
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Filled.BarChart,
                            contentDescription = "View Stats"
                        )
                    }
                    // FAB to navigate to the Habit Form screen.
                    FloatingActionButton(
                        onClick = {
                            Log.d(APP_TAG, "HabitListScreen: Navigating to Habit Form Screen")
                            navController.navigate("habitForm")
                        },
                        containerColor = Amber40,
                        contentColor = Color.Black
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Habit"
                        )
                    }
                }
            }
        }
    }
}

/**
 * Displays a single habit item.
 *
 * Shows the habit name, progress text, a progress indicator, and buttons for:
 * - Decreasing progress.
 * - Increasing progress.
 * - Deleting the habit.
 *
 * @param habit The habit to display.
 * @param onIncrement Callback invoked when the increment button is clicked.
 * @param onDecrement Callback invoked when the decrement button is clicked.
 * @param onDelete Callback invoked when the delete button is clicked.
 */
@Composable
fun HabitItem(
    habit: Habit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Amber80)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = habit.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Text(
                text = "${habit.completedToday} / ${habit.targetPerDay} completed today",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )

            val progress = if (habit.targetPerDay > 0) {
                habit.completedToday.toFloat() / habit.targetPerDay
            } else 0f

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = BlueProgress
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    // Button to decrease progress.
                    IconButton(onClick = {
                        Log.d(APP_TAG, "HabitItem: Decrease button clicked for habit '${habit.name}'")
                        onDecrement()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Decrease Progress",
                            tint = Color.Black
                        )
                    }
                    // Button to increase progress.
                    IconButton(onClick = {
                        Log.d(APP_TAG, "HabitItem: Increase button clicked for habit '${habit.name}'")
                        onIncrement()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Increase Progress",
                            tint = Color.Black
                        )
                    }
                }
                // Button to delete the habit.
                IconButton(onClick = {
                    Log.d(APP_TAG, "HabitItem: Delete button clicked for habit '${habit.name}'")
                    onDelete()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Remove Habit",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHabitListScreen() {
    val navController = rememberNavController()
    HabitListScreen(viewModel = HabitViewModel(Application()), navController = navController)
}
