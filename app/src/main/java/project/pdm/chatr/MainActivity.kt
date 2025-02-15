package project.pdm.chatr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import project.pdm.chatr.ui.HabitNavigation
import project.pdm.chatr.viewmodel.HabitViewModel

const val APP_TAG = "CHaTrApp"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: HabitViewModel = viewModel()

            HabitNavigation(navController = navController, viewModel = viewModel)
        }
    }
}
