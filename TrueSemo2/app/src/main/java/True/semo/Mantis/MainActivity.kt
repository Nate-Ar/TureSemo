package True.semo.Mantis
import True.semo.Mantis.LoginScreen
import True.semo.Mantis.SignUpScreen
import True.semo.Mantis.MainScreen
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import True.semo.Mantis.ui.theme.TrueSemoTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrueSemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val context = LocalContext.current
                    val auth = Firebase.auth

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginClick = { email, password ->
                                    authViewModel.login(email, password) { success ->
                                        if (success) {
                                            // Navigate to the main screen
                                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                                            navController.navigate("main")
                                        } else {
                                            // Show an error message
                                            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onNavigateToSignUp = { navController.navigate("signUp") }
                            )
                        }
                        composable("signUp") {
                            SignUpScreen(
                                onSignUpClick = { email, password ->
                                    authViewModel.signUp(email, password) { success ->
                                        if (success) {
                                            // Navigate to the main screen or login screen
                                            Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show()
                                            navController.navigate("login")
                                        } else {
                                            // Show an error message
                                            Toast.makeText(context, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onNavigateToLogin = { navController.navigate("login") }
                            )
                        }
                        composable("main"){
                            MainScreen()
                        }
                    }
                }
            }
        }
    }
}