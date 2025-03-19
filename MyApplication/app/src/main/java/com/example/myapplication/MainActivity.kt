package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class MainActivity : ComponentActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        var responseText by remember { mutableStateOf("Response will be shown here") }

                        Button(onClick = { fetchBackendData { response -> responseText = response } }) {
                            Text(text = "Fetch Data from Backend")
                        }

                        Text(text = responseText)
                    }
                }
            }
        }
    }

    // Function to make a network request
    private fun fetchBackendData(onResponse: (String) -> Unit) {
        Thread {
            try {
                // Log the URL being used
                Log.d("MainActivity", "Making request to http://10.0.2.2:5000")

                // Create the request
                val request = Request.Builder()
                    .url("http://10.0.2.2:5000/") // Use this for Emulator, replace with your machine IP for real devices
                    .build()

                // Execute the request
                val response: Response = client.newCall(request).execute()

                // Check the response
                if (response.isSuccessful) {
                    // Log and update the UI with the response
                    Log.d("MainActivity", "Response: ${response.body?.string()}")
                    runOnUiThread {
                        onResponse(response.body?.string() ?: "No Response")
                    }
                } else {
                    // Log and handle the error case
                    Log.e("MainActivity", "Error: ${response.code}")
                    runOnUiThread {
                        onResponse("Error: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions and log the error
                Log.e("MainActivity", "Network request failed: ${e.message}")
                runOnUiThread {
                    onResponse("Network Error")
                }
            }
        }.start()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Text(text = "Hello, World!")
    }
}
