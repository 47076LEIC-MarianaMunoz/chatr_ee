package project.pdm.chatr.ui


import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DonutLarge
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Habits") },
            label = { Text("Habits") },
            selected = false,
            onClick = { navController.navigate("habitList") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Add, contentDescription = "New Habit") },
            label = { Text("New Habit") },
            selected = false,
            onClick = { navController.navigate("habitForm") }
        )
        NavigationBarItem(
            icon = { Icon(imageVector = Icons.Filled.DonutLarge, contentDescription = "Statistics") },
            label = { Text("Statistics") },
            selected = false,
            onClick = { navController.navigate("habitStats") }
        )
    }
}
