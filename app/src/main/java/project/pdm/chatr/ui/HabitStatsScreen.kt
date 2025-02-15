package project.pdm.chatr.ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import project.pdm.chatr.model.Habit
import project.pdm.chatr.ui.theme.Amber80
import project.pdm.chatr.ui.theme.Blue40
import project.pdm.chatr.ui.theme.Blue79
import project.pdm.chatr.ui.theme.Blue80
import project.pdm.chatr.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitStatsScreen(viewModel: HabitViewModel, navController: NavHostController) {
    val habits by viewModel.habits.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Blue79)
    ) {
        // Top AppBar
        TopAppBar(
            title = { Text("Habit Progress", color = Color.Black, fontWeight = FontWeight.SemiBold) },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("habitList") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Blue79)
        )

        Spacer(modifier = Modifier.height(8.dp))

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

@Composable
fun HabitStatsCard(habit: Habit) {
    val progressFraction = if (habit.targetPerDay > 0) {
        habit.completedToday.toFloat() / habit.targetPerDay.toFloat()
    } else 0f

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
            Text(
                text = habit.name,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Gráfico Circular
            CircularProgressIndicator(
                progress = progressFraction,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp),
                color = Amber80,
                strokeWidth = 6.dp
            )

            Spacer(modifier = Modifier.height(8.dp))
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HabitStatsScreenPreview() {
    val application = Application()
    val viewModel = remember { HabitViewModel(application) }

    viewModel.addHabit(Habit(id = 1, name = "Beber água", description = "", targetPerDay = 4, completedToday = 2))
    viewModel.addHabit(Habit(id = 2, name = "Ler", description = "", targetPerDay = 2, completedToday = 1))
    viewModel.addHabit(Habit(id = 3, name = "Exercício", description = "", targetPerDay = 1, completedToday = 1))

    HabitStatsScreen(viewModel = viewModel, navController = rememberNavController())
}