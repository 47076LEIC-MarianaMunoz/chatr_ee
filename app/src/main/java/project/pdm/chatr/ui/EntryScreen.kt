package project.pdm.chatr.ui

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import project.pdm.chatr.R
import project.pdm.chatr.ui.theme.Amber40
import project.pdm.chatr.ui.theme.Blue40
import project.pdm.chatr.ui.theme.BlueGrey40

@Composable
fun EntryScreen(navController: NavHostController) {
    Log.d("EntryScreen", "Displaying Entry Screen")

    val activity = LocalContext.current as? Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Blue40, BlueGrey40)
                )
            )
    ) {
        // Info Button on the Top Right
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(
                onClick = {
                    Log.d("EntryScreen", "Navigating to Author Screen")
                    navController.navigate("author")
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "About the Author",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_chatr),
                contentDescription = "CHaTr Logo",
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 16.dp)
            )

            // Title
            Text(
                text = "Welcome to CHaTr",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Start Button
            Button(
                onClick = {
                    Log.d("EntryScreen", "Navigating to Habit List Screen")
                    navController.navigate("habitList")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Amber40, contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(55.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Get Started", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Quit Button
            Button(
                onClick = {
                    Log.d("EntryScreen", "Exiting App")
                    activity?.finish()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(55.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Quit", fontSize = 18.sp)
            }
        }
    }
}
