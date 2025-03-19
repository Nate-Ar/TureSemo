package True.semo.Mantis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val auth = FirebaseAuth.getInstance()
    val userEmail = auth.currentUser?.email ?: "Unknown User"
    val username = userEmail.substringBefore("@") // Get the part before '@'

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE)) // Gray background
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "True SEMO",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // User Icon and Username
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Using built-in AccountCircle icon
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Displaying the first part of the email (username)
            Text(
                text = username,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
