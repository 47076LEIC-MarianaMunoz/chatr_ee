package project.pdm.chatr.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import project.pdm.chatr.APP_TAG
import project.pdm.chatr.viewmodel.HabitViewModel

/**
 * Sets up the navigation graph for the app.
 *
 * @param navController The NavHostController used for navigation.
 * @param viewModel The HabitViewModel used by screens.
 */
@Composable
fun HabitNavigation(navController: NavHostController, viewModel: HabitViewModel) {
    Log.d(APP_TAG, "HabitNavigation: Initializing Navigation")

    NavHost(navController = navController, startDestination = "entry", modifier = Modifier) {
        composable("entry") {
            Log.d(APP_TAG, "EntryScreen: Navigated to Entry Screen")
            EntryScreen(navController)
        }
        composable("habitList") {
            Log.d(APP_TAG, "HabitListScreen: Navigated to Habit List Screen")
            HabitListScreen(viewModel, navController)
        }
        composable("habitForm") {
            Log.d(APP_TAG, "HabitFormScreen: Navigated to Habit Form Screen")
            HabitFormScreen(
                viewModel = viewModel,
                onHabitAdded = {
                    Log.d(APP_TAG, "HabitFormScreen: Habit added successfully")
                    navController.navigate("habitList")
                },
                onBack = {
                    Log.d(APP_TAG, "HabitFormScreen: Navigating back")
                    navController.popBackStack()
                }
            )
        }
        composable("habitStats") {
            Log.d(APP_TAG, "HabitStatsScreen: Navigated to Habit Stats Screen")
            HabitStatsScreen(viewModel, navController)
        }
        composable("author") {
            Log.d(APP_TAG, "AuthorScreen: Navigated to Author Screen")
            AuthorScreen(navController)
        }
    }
}
