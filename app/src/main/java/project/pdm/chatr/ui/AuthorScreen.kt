package project.pdm.chatr.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import project.pdm.chatr.ui.theme.Amber40
import project.pdm.chatr.ui.theme.BlueGrey80

private const val APP_TAG = "CHaTrApp"

@Composable
fun AuthorScreen(navController: NavHostController) {
    Log.d(APP_TAG, "AuthorScreen: Displaying Author Screen")

    val context = LocalContext.current
    val email = "A47076@alunos.isel.pt"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Created by: Mariana (A47076)", fontSize = 22.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(5.dp))
        Text("Contact: $email", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                Log.d(APP_TAG, "AuthorScreen: Opening email to send feedback")
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                    putExtra(Intent.EXTRA_SUBJECT, "Feedback on CHaTr App")
                }
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Amber40,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)
        ) {
            Text("Send Feedback", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                Log.d(APP_TAG, "AuthorScreen: Navigating back")
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = BlueGrey80,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)
        ) {
            Text("Back", fontSize = 18.sp)
        }
    }
}
