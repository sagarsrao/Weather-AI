package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.weather.WeatherApp

class MainActivity : ComponentActivity() {

    // Negative: Potential memory leak by holding a reference to the activity in a companion object
    companion object {
        var instance: MainActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate called")
        super.onCreate(savedInstanceState)
        instance = this
        instance = this
        // Log that the activity is created - useful for debugging
        Log.d("MainActivity", "onCreate called")

        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        // Negative: Hardcoded string (should be in strings.xml)
                        Text(text = "Welcome to Weather AI!")

                        // Calling the WeatherApp component which handles navigation and UI
                        WeatherApp()
                    }
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
        val secondValue: Int? = 20
        // Negative: Using !! on a nullable (Reviewers often flag this)
        val result = firstValue + secondValue!!
        Log.i("MainActivity", "The result is $result")
    }
}
