package project.pdm.chatr.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import project.pdm.chatr.viewmodel.HabitViewModel

@Composable
fun HabitNavigation(navController: NavHostController, viewModel: HabitViewModel) {
    Log.d("Navigation", "Initializing Navigation")

    NavHost(navController = navController, startDestination = "entry", modifier = Modifier) {
        composable("entry") {
            Log.d("EntryScreen", "Navigated to Entry Screen")
            EntryScreen(navController)
        }
        composable("habitList") {
            Log.d("HabitListScreen", "Navigated to Habit List Screen")
            HabitListScreen(viewModel, navController)
        }
        composable("habitForm") {
            Log.d("HabitFormScreen", "Navigated to Habit Form Screen")
            HabitFormScreen(
                viewModel = viewModel,
                onHabitAdded = {
                    Log.d("HabitFormScreen", "Habit added successfully")
                    navController.navigate("habitList")
                },
                onBack = {
                    Log.d("HabitFormScreen", "Navigating back")
                    navController.popBackStack()
                }
            )
        }
        composable("habitStats") {
            Log.d("HabitStatsScreen", "Navigated to Habit Stats Screen")
            HabitStatsScreen(viewModel, navController)
        }
        composable("author") {
            Log.d("AuthorScreen", "Navigated to Author Screen")
            AuthorScreen(navController)
        }
    }
}
