package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.weather.WeatherApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Log that the activity is created - useful for debugging
        Log.d("MainActivity", "onCreate called")
        
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Calling the WeatherApp component which handles navigation and UI
                    WeatherApp()
                }
            }
        }
    }
    
    /**
     * This function doesn't do anything yet but might be useful for future enhancements.
     * It demonstrates a simple calculation.
     */
    private fun unusedHelperFunction() {
        // TODO: Implement some actual logic here
        val firstValue = 10
        val secondValue = 20
        val result = firstValue + secondValue
        Log.i("MainActivity", "The result is $result")
    }
}
