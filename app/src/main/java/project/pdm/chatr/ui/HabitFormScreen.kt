package project.pdm.chatr.ui

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.pdm.chatr.APP_TAG
import project.pdm.chatr.model.Habit
import project.pdm.chatr.ui.theme.Amber80
import project.pdm.chatr.ui.theme.Blue80
import project.pdm.chatr.viewmodel.HabitViewModel


/**
 * HabitFormScreen displays a form to add a new habit with name, description, and target per day.
 *
 * @param viewModel ViewModel to add the new habit to the list.
 * @param onHabitAdded Callback to navigate back to the habit list screen after adding a habit.
 * @param onBack Callback to navigate back to the previous screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitFormScreen(
    viewModel: HabitViewModel,
    onHabitAdded: () -> Unit,
    onBack: () -> Unit
) {
    Log.d(APP_TAG, "HabitFormScreen: Displaying Habit Form Screen")

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var targetPerDay by remember { mutableStateOf("1") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top AppBar with back button
        TopAppBar(
            title = {
                Text(
                    "Add Habit",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    Log.d(APP_TAG, "HabitFormScreen: Back button clicked")
                    onBack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = CardDefaults.cardColors(containerColor = Amber80),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "New Habit",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = targetPerDay,
                        onValueChange = { targetPerDay = it },
                        label = { Text("Times per Day") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            Log.d(APP_TAG, "HabitFormScreen: Add Habit button clicked")
                            val timesPerDayInt = targetPerDay.toIntOrNull()
                            if (name.isNotBlank() && timesPerDayInt != null) {
                                viewModel.addHabit(
                                    Habit(
                                        id = System.currentTimeMillis().toInt(),
                                        name = name,
                                        description = description,
                                        targetPerDay = timesPerDayInt
                                    )
                                )
                                Log.d(APP_TAG, "HabitFormScreen: Habit added successfully")
                                onHabitAdded()
                            } else {
                                Log.d(APP_TAG, "HabitFormScreen: Invalid input, habit not added")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue80,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Add Habit", fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HabitFormScreenPreview() {
    HabitFormScreen(
        viewModel = HabitViewModel(Application()),
        onHabitAdded = {},
        onBack = {}
    )
}
