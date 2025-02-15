package project.pdm.chatr

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import project.pdm.chatr.ui.HabitNavigation
import project.pdm.chatr.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.*

const val APP_TAG = "Chatr"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain the ViewModel using ViewModelProvider (non-composable context)
        val viewModel = ViewModelProvider(this).get(HabitViewModel::class.java)

        // Check if it's a new day to shift the weekly data.
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val lastUpdate = prefs.getString("last_update", null)
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        if (lastUpdate == null || lastUpdate != today) {
            viewModel.shiftWeeklyDataForNewDay()
            prefs.edit().putString("last_update", today).apply()
        }

        setContent {
            val navController = rememberNavController()
            HabitNavigation(navController = navController, viewModel = viewModel)
        }
    }
}
