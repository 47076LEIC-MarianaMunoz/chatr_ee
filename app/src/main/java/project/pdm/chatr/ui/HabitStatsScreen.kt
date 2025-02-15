package project.pdm.chatr.ui

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import project.pdm.chatr.APP_TAG
import project.pdm.chatr.model.Habit
import project.pdm.chatr.ui.theme.Amber80
import project.pdm.chatr.ui.theme.Blue79
import project.pdm.chatr.viewmodel.HabitViewModel

/**
 * Displays the Habit Stats Screen with a TopAppBar and a list of habit stats cards.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitStatsScreen(viewModel: HabitViewModel, navController: NavHostController) {
    Log.d(APP_TAG, "HabitStatsScreen: Displaying Habit Stats Screen")
    val habits by viewModel.habits.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blue79)
    ) {
        // TopAppBar with back button and refresh icon.
        TopAppBar(
            title = {
                Text("Habit Progress", color = Color.Black, fontWeight = FontWeight.SemiBold)
            },
            navigationIcon = {
                IconButton(onClick = {
                    Log.d(APP_TAG, "HabitStatsScreen: Navigating back to Habit List")
                    navController.navigate("habitList")
                }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Blue79)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn displaying habit stats cards.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(habits) { habit ->
                HabitStatsCard(habit)
            }
        }
    }
}

/**
 * Displays a card showing a single habit's statistics, including today's progress and a weekly summary.
 */
@Composable
fun HabitStatsCard(habit: Habit) {
    Log.d(APP_TAG, "HabitStatsCard: Displaying stats for habit '${habit.name}'")

    // Calculate today's progress fraction.
    val progressFraction = if (habit.targetPerDay > 0)
        habit.completedToday.toFloat() / habit.targetPerDay.toFloat() else 0f

    // Calculate the weekly total completions.
    val weeklyTotal = habit.weeklyCompletions.sum()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Habit name.
            Text(
                text = habit.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Circular progress indicator for today's progress.
            CircularProgressIndicator(
                progress = progressFraction,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp),
                color = Amber80,
                strokeWidth = 6.dp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Display today's progress.
            Text(
                text = "Today: ${habit.completedToday} / ${habit.targetPerDay}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Text(
                text = "Progress: ${(progressFraction * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Display weekly total.
            Text(
                text = "Weekly Total: $weeklyTotal",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Display day-by-day completions with current day highlighted.
            WeeklyDetailRow(weeklyCompletions = habit.weeklyCompletions)
        }
    }
}

/**
 * Displays a row with day labels (Mon..Sun) and the corresponding completions from weeklyCompletions.
 * The current day is highlighted.
 */
@Composable
fun WeeklyDetailRow(weeklyCompletions: List<Int>) {
    // Day names for each index.
    val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    // Get current day index using Calendar.
    val calendar = java.util.Calendar.getInstance()
    val currentDayIndex = (calendar.get(java.util.Calendar.DAY_OF_WEEK) + 5) % 7

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0..6) {
            val isToday = i == currentDayIndex
            val textColor = if (isToday) Color.Red else Color.Black
            val fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = dayNames[i],
                    fontWeight = fontWeight,
                    color = textColor
                )
                Text(
                    text = weeklyCompletions[i].toString(),
                    fontWeight = fontWeight,
                    color = textColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HabitStatsScreenPreview() {
    val application = Application()
    val viewModel = HabitViewModel(application)
    HabitStatsScreen(viewModel = viewModel, navController = rememberNavController())
}
